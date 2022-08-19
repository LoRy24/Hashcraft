package com.github.lory24.hashcraft.api.plugin;

import java.io.File;

/**
 * This class will be used to register and unregister plugins from the server.
 */
public abstract class PluginsManager {

    /**
     * With this function you will be able to load a plugin into the server
     *
     * @param file The plugin's file
     */
    public abstract void loadPlugin(final File file);

    /**
     * This function will unload a plugin form the server. It will also provide to kill all the async tasks registered by
     * the plugin in the scheduler & to unregister all the plugin's commands and events.
     *
     * @param pluginsName The name of the plugin that should be disabled
     */
    public abstract void unloadPlugin(final String pluginsName);

    /**
     * With this function you can obtain the plugin main class instance just by the name of the plugin.
     *
     * @param pluginName The name of the plugin
     * @return The plugin's main class instanced object
     */
    public abstract ProxyPlugin getProxyPlugin(String pluginName);
}
