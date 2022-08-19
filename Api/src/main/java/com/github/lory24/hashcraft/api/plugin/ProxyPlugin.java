package com.github.lory24.hashcraft.api.plugin;

import com.github.lory24.hashcraft.api.Proxy;
import lombok.Getter;
import lombok.Setter;

/**
 * This class will be extended by the main classes of a plugin.
 *
 * @author LoRy24
 */
public abstract class ProxyPlugin {

    /**
     * The plugin's description (the infos stored in the plugin.yml file). Updated when loading the plugin
     */
    @Getter @Setter
    private PluginsDescription description;

    /**
     * A proxyserver reference
     */
    @Getter
    private final Proxy proxy = Proxy.getInstance();

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
