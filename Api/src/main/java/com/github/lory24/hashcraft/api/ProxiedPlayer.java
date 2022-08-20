package com.github.lory24.hashcraft.api;

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
}
