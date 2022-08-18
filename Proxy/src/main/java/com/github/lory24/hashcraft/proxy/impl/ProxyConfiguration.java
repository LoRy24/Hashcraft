package com.github.lory24.hashcraft.proxy.impl;

import com.github.lory24.hashcraft.proxy.Hashcraft;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum ProxyConfiguration {

    PROXY_HOST("settings.host"),
    PORXY_PORT("settings.port"),
    SHOUD_SEND_PING_NOTIFICATIONS("settings.sendPingMessage"),
    SERVER_MESSAGE_OF_THE_DAY("settings.serverListPing.messageOfTheDay"),
    ;

    /**
     * The path in the config. Used by the GET function to obtain the path where to load the data from. Initialized
     * in the constructor of this enum.
     */
    @Getter
    private final String path;

    /**
     * This function will obtain an object form the config, using the path defined in this class
     */
    public Object get() {
        return ((Hashcraft) Hashcraft.getInstance()).getHashcraftConfiguration().getYamlConfigurationData().get(this.path);
    }

    /**
     * This function will obtain a string with parsed colors from the config. Parsed colors means that the caracters § and &
     * will be replaced with the code \u00a7.
     */
    @NotNull
    public String getStringWithColors() {
        return  ((String) this.get()).replace("§", "\u00a7")
                .replace("&", "\u00a7");
    }
}
