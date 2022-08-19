package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.plugin.PluginsDescription;
import com.github.lory24.hashcraft.api.plugin.PluginsManager;
import com.github.lory24.hashcraft.api.plugin.ProxyPlugin;
import com.github.lory24.hashcraft.api.yml.YamlConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The plugins' manager impl
 */
@RequiredArgsConstructor
public class HashcraftPluginsManager extends PluginsManager {

    /**
     * Hashcraft reference
     */
    @Getter
    private final Hashcraft hashcraft;

    /**
     * A hashmap containing all the plugin main classes
     */
    @Getter
    private final HashMap<String, ProxyPlugin> pluginsMainClassesInstances = new HashMap<>();

    /**
     * A hashmap containing all the plugin's classes
     */
    @Getter
    private final HashMap<String, List<Class<?>>> registeredPluginsClasses = new HashMap<>();

    /**
     * Protected function used by the proxy to load all the plugins stored in the 'plugins' folder
     */
    protected void loadAllPlugins() {
        // Load all the plugins in the plugins folder
        Arrays.stream(Objects.requireNonNull(new File("./plugins").listFiles())).filter(file -> file.getName().endsWith(".jar")).forEach(this::loadPlugin);
    }


    /**
     * With this function you will be able to load a plugin into the server
     *
     * @param file The plugin's file
     */
    @SneakyThrows
    @Override
    public void loadPlugin(@NotNull File file) {
        // Notify that the plugin is getting loaded
        this.hashcraft.getLogger().info("Enabling plugin: " + file.getName());

        // Load the data and start the plugin on the main thread
        try {
            final PluginsDescription description = this.loadPluginData(file); // Load the data

            // Call the onEnable function.
            this.pluginsMainClassesInstances.get(description.name()).onEnable();
        }
        catch (IOException | ClassNotFoundException e) { // If an error occours
            // Throw a new plugin load exception
            throw new PluginLoadException("A generic error has occoured while loading the plugin " + file.getName() + ": " + e.getLocalizedMessage());
        }
        catch (PluginLoadException e) {
            // Print the plugin load exception
            e.printStackTrace();
        }
    }

    /**
     * This function will unload a plugin form the server. It will also provide to kill all the async tasks registered by
     * the plugin in the scheduler & to unregister all the plugin's commands and events.
     *
     * @param pluginsName The name of the plugin that should be disabled
     */
    @SneakyThrows
    @Override
    public void unloadPlugin(String pluginsName) {

        // If the plugin doesn't exist, throw an exception
        if (this.getProxyPlugin(pluginsName) == null) throw new PluginUnloadException("Error while disabling the plugin " + pluginsName + ": Plugin not found");

        // Notify that the plugin is going to be disabled
        this.hashcraft.getLogger().info("Disabling plugin " + pluginsName);

        // Call the onDisable function
        this.pluginsMainClassesInstances.get(pluginsName).onDisable();

        // Unregister the plugin's services
        this.unregisterPluginFromServices(this.getProxyPlugin(pluginsName));

        // Remove the plugin form the internal hashmaps
        this.pluginsMainClassesInstances.remove(pluginsName);
        this.registeredPluginsClasses.remove(pluginsName);
    }

    /**
     * With this function you can obtain the plugin main class instance just by the name of the plugin.
     *
     * @param pluginName The name of the plugin
     * @return The plugin's main class instanced object
     */
    @Override
    public ProxyPlugin getProxyPlugin(String pluginName) {
        return this.pluginsMainClassesInstances.get(pluginName); // Get the plugin instance
    }

    /**
     * This function will be used by the loadPlugin function when he's going to laod the plugin's data.
     *
     * @param plugin The plugin file
     * @return The plugin's description object, containing all the infos of the pl
     */
    protected PluginsDescription loadPluginData(@NotNull final File plugin) throws IOException,
            PluginLoadException, ClassNotFoundException {
        // Obtain the Jar file and the URLClassLoader
        final JarFile jar = new JarFile(plugin.getAbsoluteFile());
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + plugin.getAbsolutePath() + "!/")});

        // Load the Yaml config and create the plugin description object
        YamlConfig pluginConfig = this.loadPluginsYamlConfig(classLoader, plugin);
        PluginsDescription pluginsDescription = new PluginsDescription((String) pluginConfig.get("name"), (String) pluginConfig.get("version"), (String) pluginConfig.get("mainClass"), (String) pluginConfig.get("author"));

        // Check for missing description values (except. autor)
        if (pluginsDescription.name() == null || pluginsDescription.version() == null ||
                pluginsDescription.mainClassPath() == null) {
            throw new PluginLoadException("Error while loading " + plugin.getName() + ": missing fondamental values");
        }

        // Load the classes
        final List<Class<?>> classes = new ArrayList<>();
        ProxyPlugin proxyPlugin = null; // Initialize with null value

        // Classes loading loop
        Enumeration<JarEntry> jarEntries = jar.entries(); // Read all the entries from the jar file
        while (jarEntries.hasMoreElements()) {
            JarEntry entry = jarEntries.nextElement(); // Read the next entry

            // If the entry is a class
            if (entry.isDirectory()) continue;
            if (entry.getName().endsWith(".class")) {
                // Load the class's name
                String className = entry.getName().substring(0, entry.getName().length() -6).replace('/', '.'); // Get the class name

                // Create the class object
                Class<?> clazz = classLoader.loadClass(className);
                classes.add(clazz);

                // Check if the class name is equal to the main plugin's class path, else continue
                if (!className.equals(pluginsDescription.mainClassPath())) continue;

                // Try to set the plugins main class instance
                try {
                    // Instance by the empty constructor, else trow an exception
                    proxyPlugin = (ProxyPlugin) clazz.getConstructor().newInstance();

                    // Set proxy plugin internal fields
                    proxyPlugin.setDescription(pluginsDescription);
                }
                catch (Exception ignored) {
                    throw new PluginLoadException("Error while loading the plugin " + pluginsDescription.name() + "'s main class: Does it have any constructor parameter?");
                }
            }
        }

        // Close the jar file
        jar.close();

        // If the plugin's main class is null, throw an error
        if (proxyPlugin == null)
            throw new PluginLoadException("Error whie loading the plugin" + pluginsDescription.name() + ": main class not found.");

        // Store the plugin inside this class
        this.pluginsMainClassesInstances.put(pluginsDescription.name(), proxyPlugin);
        this.registeredPluginsClasses.put(pluginsDescription.name(), classes);

        // Return the plugin's description
        return pluginsDescription;
    }

    /**
     * This function will unregister the plugin from all the services that he uses.
     *
     * @param plugin The plugin's obj
     */
    public void unregisterPluginFromServices(final ProxyPlugin plugin) {
        // Unregister the plugin from the scheduler
        ((HashcraftScheduler) this.hashcraft.getScheduler()).unregisterPlugin(plugin); // Scheduler

        // Other services goes here
    }

    /**
     * Load the Yaml config file from a plugin.
     *
     * @param pluginsClassLoader The classloader from where to get the plugin.yml resource
     * @param plugin The plugin's file
     * @return The Yaml configuration object
     */
    private YamlConfig loadPluginsYamlConfig(@NotNull final URLClassLoader pluginsClassLoader, final File plugin)
            throws PluginLoadException, IOException {
        // Try to load the plugin's yaml config
        final InputStream pluginsYamlConfigInputStream = pluginsClassLoader.getResourceAsStream("plugin.yml");
        if (pluginsYamlConfigInputStream == null) throw new PluginLoadException("Error while loading " + plugin.getName() + ": is that a plugin?");

        // Load the config and close the input stream
        final YamlConfig config = new YamlConfig().loadConfig(new String(pluginsYamlConfigInputStream.readAllBytes()));
        pluginsYamlConfigInputStream.close();

        // Return the configuration
        return config;
    }

    /**
     * This protected function will unload all the plugins
     */
    protected void unloadAllPlugins() {
        // Unload all the plugin
        this.pluginsMainClassesInstances.forEach((pluginsName, plugin) -> this.unloadPlugin(pluginsName));
    }
}
