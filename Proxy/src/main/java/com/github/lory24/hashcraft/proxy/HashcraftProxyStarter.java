package com.github.lory24.hashcraft.proxy;

/**
 * This class will set up all the proxy configuration, and then it will load the effective netty server. If any error
 * occurs, it will immediately kill the main thread and then the server will be stopped. The function to start the server
 * is the {@code startHashcraftProxy()} one
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class HashcraftProxyStarter {

    /**
     * Load the configuration and start the Hashcraft proxy. This function is called from the Bootstrap's main class.
     */
    public static void startHashcraftProxy() {

    }
}
