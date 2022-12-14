package com.github.lory24.hashcraft.proxy.impl;

import com.github.lory24.hashcraft.api.yml.YamlConfig;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;

public class HashcraftProxyConfiguration {

    /**
     * The config file
     */
    @Getter
    private final File configFile;

    /**
     * The loaded YAML data
     */
    @Getter
    private YamlConfig yamlConfigurationData;

    /**
     * Inject the config file
     * @param configFile The file where all the proxy configuration is stored
     */
    public HashcraftProxyConfiguration(File configFile) {
        this.configFile = configFile;
    }

    /**
     * This function will load the configuration from the config.yml file.
     */
    public void loadConfiguration() {
        try {
            // Load the config
            this.yamlConfigurationData = new YamlConfig().loadConfig(this.configFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
