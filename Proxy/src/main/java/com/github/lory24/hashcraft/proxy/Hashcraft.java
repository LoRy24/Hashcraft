package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.api.util.ProxyConfiguration;
import com.github.lory24.hashcraft.proxy.impl.HashcraftProxyConfiguration;
import com.github.lory24.hashcraft.proxy.logger.CustomLoggerPrintStream;
import com.github.lory24.hashcraft.proxy.logger.HashcraftLogger;
import com.github.lory24.hashcraft.proxy.netty.HashcraftChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.io.File;
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

    @Getter
    private final File configFile;

    /**
     * The Hashcraft proxy configuration object. Contains all the settings of the proxy.
     */
    @Getter
    private HashcraftProxyConfiguration hashcraftConfiguration;

    /**
     * Inject the values instanced in the HashcraftProxyStarter
     *
     * @param customLoggerPrintStream The custom printStream object. Used to capture "everything" that is printed in the console
     * @param hashcraftLogger The logger object
     */
    public Hashcraft(CustomLoggerPrintStream customLoggerPrintStream, HashcraftLogger hashcraftLogger) {
        this.customLoggerPrintStream = customLoggerPrintStream;
        this.hashcraftLogger = hashcraftLogger;
        this.configFile = new File("./config.yml");
    }

    /**
     * Protected function that will start the Proxy server.
     */
    protected void start() {

        // Load the Proxy configuration
        this.getLogger().info("Loading configuration... ");
        this.hashcraftConfiguration = new HashcraftProxyConfiguration(this.configFile); // Instance the hashcraft configuration object
        this.hashcraftConfiguration.loadConfiguration(); // Load data
        this.getLogger().info("Configuration loaded! Starting the proxy");

        // Launch the netty server
        this.startListening();
    }

    /**
     * Start all the listeners and run the server
     */
    private void startListening() {
        // Define the groups
        final EventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();

        // Set up the serverBootstrap and start listening
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HashcraftChannelInitializer());

            // Bind the server
            serverBootstrap.bind(this.hashcraftConfiguration.getProxyPort()).sync().channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e); // Error while running sync func.
        } finally {
            // Shutdown the groups
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * Get the logger object instanced in the Proxy class. The logger is not a fully custom one, but is an implementation
     * of the default java.util's logger, in order to make the core more optimized (no Log4j :D).
     */
    @Override
    public Logger getLogger() {
        return Hashcraft.this.hashcraftLogger; // Return the instanced logger obj
    }

    /**
     * Return the instanced ProxyConfiguration object.
     *
     * @see ProxyConfiguration
     */
    @Override
    public ProxyConfiguration getProxyConfiguration() {
        return Hashcraft.this.hashcraftConfiguration; // Return the instanced proxy configuration obj
    }
}
