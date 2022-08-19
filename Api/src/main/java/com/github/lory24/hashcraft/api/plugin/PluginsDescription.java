package com.github.lory24.hashcraft.api.plugin;

import org.jetbrains.annotations.Nullable;

/**
 * This class will be used to give a proper description to a plugin. The most important infos of the plugin are also
 * stored in this class, for example the plugin's name, or the plugin's main class. All of these infos are stored in the
 * plugin.yml file stored in the plugin jar file.
 *
 * @param name The name of the plugin
 * @param version The version of the plugin
 * @param mainClassPath The path to the main class (without the .java at the end)
 * @param author The author of the plugin
 */
public record PluginsDescription(String name, String version, String mainClassPath, @Nullable String author) {

}
