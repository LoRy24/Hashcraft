package com.github.lory24.hashcraft.api;

import com.github.lory24.hashcraft.api.yml.YamlConfig;
import org.junit.Assert;
import org.junit.Test;

public class YamlConfigTest {

    @Test
    public void testSet() {
        // Load and update the name value
        YamlConfig config = new YamlConfig().loadConfig("Settings:\n  Name: \"LoRy24\"");
        config.set("Settings.Name", "LoRy25");

        // Check if the value is LoRy25
        Assert.assertEquals(config.get("Settings.Name"), "LoRy25");
    }
}
