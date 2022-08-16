package com.github.lory24.hashcraft.api;

import com.github.lory24.hashcraft.api.scheduler.Scheduler;
import com.github.lory24.hashcraft.api.util.ProxyConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

/**
 * The Proxy class is the abstract class that is extended by the Hashcraft proxy object. It will provide to the plugin
 * the abilty to obtain infos from the Proxy object without direct access to the initiaized object in the JVM. It's also
 * good because it decreases the size of the plugin, making it faster and lighter. Obiouvsly, to access to the instanced
 * Proxy abstract object, you have to obtain a global instanced object. This object is the "instance" one, contained
 * in this class. It's instanced when the proxy starts, and it points to the Proxy object. You can access it using
 * its getter.
 *
 * @author LoRy24
 * @apiNote VULN #1: A PLUGIN CAN CHANGE THE INSTANCE OBJECT, MAKING THE API NOT FUNCTIONAL. SHOULD BE FIXED WITH
 * DEPENDENCY INJECTION, MAYBE IN A NEW VERSION
 * @version 1.0.0
 */
public abstract class Proxy {

    /**
     * The global level instanced Proxy object. Instanced when the proxy server starts.
     */
    @Getter @Setter
    private static Proxy instance;

    /**
     * Get the logger object instanced in the Proxy class. The logger is not a fully custom one, but is an implementation
     * of the default java.util's logger, in order to make the core more optimized (no Log4j :D).
     */
    public abstract Logger getLogger();

    /**
     * Return the instanced ProxyConfiguration object.
     * @see ProxyConfiguration
     */
    public abstract ProxyConfiguration getProxyConfiguration();

    /**
     * Return the scheduler object
     */
    public abstract Scheduler getScheduler();
}
