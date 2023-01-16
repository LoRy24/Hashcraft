package com.github.lory24.hashcraft.api;

import com.github.lory24.hashcraft.api.util.SubServer;

/**
 * The ProxiedPlayer is an object that represent a player connected to the Hashcraft proxy. This class is implemented
 * in the proxy in order to do connection features. An important feature of this object is the one that connects the
 * player to other sub server.
 *
 * @author LoRy24
 * @see Proxy
 */
public abstract class ProxiedPlayer {

    /**
     * Return the name of the player
     */
    public abstract String getUsername();

    /**
     * Connects a player to a proxy sub-server. The server's info should be put in a SubServer object.
     *
     * @param subServer The sub-server where to connect the player
     */
    public abstract void connect(final SubServer subServer);

    /**
     * Returns the server where the player is connected. Null if he isn't connected anywhere
     */
    public abstract SubServer getCurrentServer();
}
