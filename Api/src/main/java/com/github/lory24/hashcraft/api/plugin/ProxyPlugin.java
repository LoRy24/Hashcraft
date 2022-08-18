package com.github.lory24.hashcraft.api.plugin;

/**
 * This class will be extended by the main classes of a plugin.
 *
 * @author LoRy24
 */
public abstract class ProxyPlugin {

    /**
     * This function will be called when the plugin enables. It will be called on a new thread by the Hashcraft plugins'
     * manager.
     */
    public void onEnable() {}

    /**
     * This function will be called when the Hashcraft plugins' manager will disable the plugin.
     */
    public void onDisable() {}
}
