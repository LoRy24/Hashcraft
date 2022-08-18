package com.github.lory24.hashcraft.api.util;

import com.github.lory24.hashcraft.chatcomponent.TextChatComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

/**
 * The ServerListPingResponse is the object that will be sent back during the status process. This will be converted in
 * a json object before sending to the client. See <a href="https://wiki.vg/Server_List_Ping">https://wiki.vg/Server_List_Ping</a>
 * for more details
 *
 * @param version       The version field
 * @param players       The players field
 * @param description   The description ChatComponent field
 * @param favicon       The favicon encoded in base64, with this format: data:image/png;base64,[base64] (without '[' & ']')
 */
public record ServerListPingResponse(@NotNull ServerListPingResponse.ServerListVersion version, @NotNull ServerListPingResponse.ServerListPlayers players, @NotNull TextChatComponent description, @Nullable String favicon) {

    /**
     * This is the first parameter of the ServerListPingResponse. This value will represent the version of the proxy, including
     * the name and the protocol.
     */
    @AllArgsConstructor
    public static class ServerListVersion {

        /**
         * This value represents the version that should be sent to the client
         */
        @Getter @Setter
        private String name;

        /**
         * This value represents the version's protocol id
         */
        @Getter @Setter
        private int protocol;
    }

    /**
     * This class will reperesent a sample player that will be used in the "Players" section in the JSON response. This will
     * store the username and the uuid of the player.
     */
    @AllArgsConstructor
    public static class SamplePlayer {

        /**
         * The name of the player
         */
        @Getter @Setter
        private String name;

        /**
         * The uuid of the player (with '-')
         */
        @Getter @Setter
        private String uuid;
    }

    /**
     * This class will contain the data that should put in the "Players" JSON section.
     */
    @AllArgsConstructor
    public static class ServerListPlayers {

        /**
         * The maximum amount of players (can be fake)
         */
        @Getter @Setter
        private int max;

        /**
         * The amount of online players
         */
        @Getter @Setter
        private int online;

        /**
         * The samples players
         */
        @Getter @Setter
        private SamplePlayer[] sample;
    }

    /*
     * SOME UTILITY FUNCTIONS
     */

    /**
     * Create a favicon string from base64
     *
     * @param base64 The favicon encoded in base64 format
     */
    @NotNull
    public static String convertBase64ToFavIcon(@NotNull final Base64 base64) {
        return "data:image/png;base64," + base64;
    }
}
