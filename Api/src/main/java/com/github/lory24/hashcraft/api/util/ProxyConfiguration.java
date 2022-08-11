package com.github.lory24.hashcraft.api.util;

/**
 * The ProxyConfiguration class is used by the Hashcraft proxy to store all the settings written in the config.yml file.
 * So, all the data is saved in a ProxyConfiguration impl and can be accessed though the methods that are written into
 * this class. You can obtain this instanced object from the Proxy instance.
 *
 * @author LoRy24
 * @see com.github.lory24.hashcraft.api.Proxy
 * @see com.github.lory24.hashcraft.api.yml.YamlConfig
 */
public abstract class ProxyConfiguration {

    /**
     * Obtain the host where the proxy should be hosted
     */
    public abstract String getProxyHost();

    /**
     * Obtain the port where the proxy should be hosted on
     */
    public abstract int getProxyPort();
}
