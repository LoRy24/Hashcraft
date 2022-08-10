package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.proxy.logger.CustomLoggerPrintStream;
import com.github.lory24.hashcraft.proxy.logger.HashcraftLogger;
import lombok.Getter;

import java.util.logging.Logger;

/**
 * The Hashcraft proxy object. This is the core of the server
 */
public class Hashcraft extends Proxy {

    /*
     * Internal fields. Used only for internal usage OR api
     */

    @Getter
    private final CustomLoggerPrintStream customLoggerPrintStream;

    @Getter
    private final HashcraftLogger hashcraftLogger;

    /**
     * Inject the values instanced in the HashcraftProxyStarter
     *
     * @param customLoggerPrintStream The custom printStream object. Used to capture "everything" that is printed in the console
     * @param hashcraftLogger The logger object
     */
    public Hashcraft(CustomLoggerPrintStream customLoggerPrintStream, HashcraftLogger hashcraftLogger) {
        this.customLoggerPrintStream = customLoggerPrintStream;
        this.hashcraftLogger = hashcraftLogger;
    }

    /**
     * Protected function that will start the Proxy server.
     */
    protected void start() {
        this.getLogger().info("Hello World!");
    }

    /**
     * Get the logger object instanced in the Proxy class. The logger is not a fully custom one, but is an implementation
     * of the default java.util's logger, in order to make the core more optimized (no Log4j :D).
     */
    @Override
    public Logger getLogger() {
        return Hashcraft.this.hashcraftLogger; // Return the instanced logger obj
    }
}
