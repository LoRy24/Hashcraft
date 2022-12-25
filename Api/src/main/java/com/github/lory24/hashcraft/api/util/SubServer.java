package com.github.lory24.hashcraft.api.util;

/**
 * The SubServer utility class is used to store data about the registered sub-servers of the network.
 *
 * @param name The name of the sub-server.
 * @param host The host of the sub-server. Doesn't contain the port
 * @param port The port of the sub-server.
 */
public record SubServer(String name, String host, int port) {
}
