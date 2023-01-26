package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.api.plugin.PluginsManager;
import com.github.lory24.hashcraft.api.scheduler.Scheduler;
import com.github.lory24.hashcraft.api.util.SubServer;
import com.github.lory24.hashcraft.proxy.features.ConnectionThrottleSystem;
import com.github.lory24.hashcraft.proxy.impl.HashcraftProxyConfiguration;
import com.github.lory24.hashcraft.proxy.impl.ProxyConfiguration;
import com.github.lory24.hashcraft.proxy.logger.CustomLoggerPrintStream;
import com.github.lory24.hashcraft.proxy.logger.HashcraftLogger;
import com.github.lory24.hashcraft.proxy.netty.HashcraftChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * The Hashcraft proxy object. This is the core of the server
 */
@Getter
public class Hashcraft extends Proxy {

    /*
     * Internal fields. Used only for internal usage OR api
     */

    private final CustomLoggerPrintStream customLoggerPrintStream;

    private final HashcraftLogger hashcraftLogger;

    private final File configFile;

    /**
     * The Hashcraft proxy configuration object. Contains all the settings of the proxy.
     */
    private HashcraftProxyConfiguration hashcraftConfiguration;

    /**
     * The scheduler obj
     */
    private HashcraftScheduler hashcraftScheduler;

    /**
     * The plugins' manager obj
     */
    private HashcraftPluginsManager hashcraftPluginsManager;

    /**
     * A hashmap containing all the connected players
     */
    private final HashMap<String, HashcraftPlayer> players = new HashMap<>();

    /**
     * The connection throttle system. Used in the client -> proxy channel
     */
    private final ConnectionThrottleSystem connectionThrottleSystem;

    /**
     * The HashMap containing all the sub servers objects identified by the name of each server
     */
    private final HashMap<String, SubServer> subServerInfos = new HashMap<>();

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
        this.connectionThrottleSystem = new ConnectionThrottleSystem(this);
    }

    /**
     * Protected function that will start the Proxy server.
     */
    protected void start() {

        // Set the Proxy INSTANCE in the API
        Proxy.setInstance(this);

        // Add the shutdown task
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        // Load the scheduler
        this.getLogger().info("Loading the scheduler...");
        this.hashcraftScheduler = new HashcraftScheduler();
        this.getLogger().info("Scheduler loaded! Loading configuration... ");

        // Load the Proxy configuration
        this.hashcraftConfiguration = new HashcraftProxyConfiguration(this.configFile); // Instance the hashcraft configuration object
        this.hashcraftConfiguration.loadConfiguration(); // Load data
        this.getLogger().info("Configuration loaded! Loading servers...");

        // Load the servers from the configuration and check if everything went fine
        if (!loadServersFromConfig() && this.getOnJoinServer() == null) {
            this.getLogger().info("Error while loading sub-servers! Closing the proxy...");
            Runtime.getRuntime().exit(0);
            return;
        }

        // Send messages
        this.debug("Loaded servers: " + this.subServerInfos.keySet());
        this.getLogger().info("Servers loaded! Loading plugins...");

        // Load the plugins
        this.hashcraftPluginsManager = new HashcraftPluginsManager(this);
        this.hashcraftPluginsManager.loadAllPlugins();
        this.getLogger().info("Plugins loaded! Starting the proxy...");

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

            // Copy the host string & the port value
            final String host = (String) ProxyConfiguration.PROXY_HOST.get();
            final int port = (int) ProxyConfiguration.PORXY_PORT.get();

            // Create the listener object
            ChannelFutureListener channelFutureListener = channelFuture -> {
                // If the listener has been started
                if (channelFuture.isSuccess()) {
                    this.getLogger().info("Listening on " + host + ":" + port);
                    return;
                }

                // Otherwise
                this.getLogger().warning("Error while listening on " + host + ". Error message: " + channelFuture.cause());
            };

            // Start the bootstrap
            new ServerBootstrap()
                    .group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new HashcraftChannelInitializer(this)).localAddress(new InetSocketAddress(host, port)).bind().addListener(channelFutureListener)
                    .sync().channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e); // Error while running sync func.
        }
        finally {
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
        return this.hashcraftLogger; // Return the instanced logger obj
    }

    /**
     * Return the scheduler object
     */
    @Override
    public Scheduler getScheduler() {
        return this.hashcraftScheduler; // Return the scheduler
    }

    /**
     * Return the plugins' manager instanced object
     */
    @Override
    public PluginsManager getPluginsManager() {
        return this.hashcraftPluginsManager; // Return the ins
    }

    /**
     * This function will be executed when the software closes. It will also save the logs before the software loses them.
     */
    private void onShutdown() {
        // Unload all the plugins
        if (this.hashcraftPluginsManager != null) {
            this.getLogger().info("Closing the server: Disabling the plugins...");
            this.hashcraftPluginsManager.unloadAllPlugins();
        }

        // Cancel all the tasks
        if (this.hashcraftScheduler != null) {
            this.getLogger().info("Cancelling all the scheduler tasks...");
            this.hashcraftScheduler.cancelAllTasks();
        }

        // Save the logger before closing
        if (this.hashcraftLogger != null) {
            this.getLogger().info("Saving logs...");
            this.hashcraftLogger.saveLogger();
        }

        // Final notify
        System.out.println("Done. Bye!");
    }

    /**
     * This internal function loads the servers written in the config file and puts them in the HashMap defined in this
     * class.
     */
    @SuppressWarnings("unchecked")
    private boolean loadServersFromConfig() {
        try {
            HashMap<String, HashMap<String, Object>> servers = (HashMap<String, HashMap<String, Object>>) ProxyConfiguration.SERVERS.get();
            AtomicBoolean status = new AtomicBoolean(true);

            // Register the servers
            servers.forEach((serverName, settings) -> {
                if (!settings.containsKey("host") || !settings.containsKey("port")) {
                    this.getLogger().warning("Invalid settings for server \"" + serverName + "\"! Check config before starting the server again.");
                    status.set(false);
                    return;
                }

                // Register the sub-server
                this.subServerInfos.put(serverName, new SubServer(serverName, (String) settings.get("host"),
                        (Integer) settings.get("port")));
            });

            // Success!
            return status.get();
        } catch (Exception ignored) {
            // If an error occurs, returns false
            return false;
        }
    }

    /**
     * Sends a debug message only if it's enabled in the config file.
     *
     * @param message The debug message
     */
    public void debug(String message) {
        // If enabled, send the message
        if (ProxyConfiguration.DEBUG.getBoolean()) this.getLogger().info("[DEBUG] " + message);
    }

    /**
     * Return the amount of online players
     */
    @Override
    public int getPlayersCount() {
        return this.players.size();
    }

    /**
     * This functions returns the info of a registered server in the network.
     *
     * @param name The name of the registered server
     */
    @Override
    public SubServer getServer(String name) {
        return this.subServerInfos.get(name);
    }

    /**
     * This function returns the onJoin sub-server, set in the proxy configuration
     */
    public SubServer getOnJoinServer() {
        return this.subServerInfos.get((String) ProxyConfiguration.ON_JOIN_SERVER.get());
    }
}
