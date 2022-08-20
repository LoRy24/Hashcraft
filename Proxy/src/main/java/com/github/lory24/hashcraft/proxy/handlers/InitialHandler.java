package com.github.lory24.hashcraft.proxy.handlers;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.api.util.ServerListPingResponse;
import com.github.lory24.hashcraft.chatcomponent.TextChatComponent;
import com.github.lory24.hashcraft.protocol.ProtocolUtils;
import com.github.lory24.hashcraft.protocol.packet.*;
import com.github.lory24.hashcraft.protocol.packet.legacy.LegacyHandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.legacy.LegacyPingPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginDisconnectPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginSetCompressionPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginStartPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginSuccessPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusPingPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusRequestPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusResponsePacket;
import com.github.lory24.hashcraft.proxy.Hashcraft;
import com.github.lory24.hashcraft.proxy.HashcraftPlayer;
import com.github.lory24.hashcraft.proxy.impl.ProxyConfiguration;
import com.github.lory24.hashcraft.proxy.netty.ChannelWrapper;
import com.github.lory24.hashcraft.proxy.netty.HashcraftProxyHandler;
import com.github.lory24.hashcraft.proxy.netty.PacketHandler;
import com.github.lory24.hashcraft.proxy.utils.InitialHandlerState;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
public class InitialHandler extends PacketHandler {

    /**
     * An instance of the proxy obj
     */
    @Getter
    private final Hashcraft proxy;

    /**
     * The ChannelWrapper obj
     */
    @Getter
    private ChannelWrapper channelWrapper;

    /**
     * The current initial handler state
     */
    @Getter @Setter
    private InitialHandlerState state = InitialHandlerState.HANDSHAKE;

    /**
     * A handshake reference
     */
    @Getter
    private HandshakePacket handshake;

    /**
     * If the InitialHandler is processing with legacy features
     */
    @Getter
    private boolean legacy;

    /**
     * If the login connection should be started in offline mode
     */
    @Getter @Setter
    private boolean onlineMode = false;

    /**
     * Gson instanced obj
     */
    @Getter
    private final Gson gson = new Gson();

    /**
     * Login start packet reference
     */
     @Getter
     private LoginStartPacket loginStart;

    /**
     * This function will handle the handshake packet.
     *
     * @param handshakePacket The handshake packet
     */
    @Override
    public void handle(@NotNull HandshakePacket handshakePacket) {

        // If the proxy isn't processing the handshake state, notify it and close the channel
        if (this.state != InitialHandlerState.HANDSHAKE) {
            this.proxy.getLogger().warning("Not expecting Handshake packet during " + this.state + " state.");
            this.channelWrapper.close();
            return;
        }

        // Set the handshake copy
        this.handshake = handshakePacket;

        // Process the handshake based on the nextstate id
        switch (handshakePacket.getNextState()) {

            // Status: Set up the status state and notify the ping
            case 1 -> {
                // Notify if enabled
                if (Boolean.parseBoolean(String.valueOf(ProxyConfiguration.SHOUD_SEND_PING_NOTIFICATIONS.get()))) this.getProxy().getLogger().info(this.channelWrapper.getRemoteAddress() + " has pinged.");
                this.setState(InitialHandlerState.STATUS);
                this.channelWrapper.updateProtocolUtils(ProtocolUtils.STATUS);
            }


            // Login
            case 2 -> {
                // Init the login state
                this.setState(InitialHandlerState.LOGIN);
                this.channelWrapper.updateProtocolUtils(ProtocolUtils.LOGIN);

                // Notify the incoming connection
                this.getProxy().getLogger().info(this.channelWrapper.getRemoteAddress() + " has connected.");
            }


            // Not expected: Close the channel and notify it
            default -> {
                this.channelWrapper.close();
                this.proxy.getLogger().warning("Received an invalid NextState id: " + this.handshake.getNextState());
            }
        }
    }

    /**
     * This function will handle the status request packet
     *
     * @param statusRequestPacket The request packet
     */
    @SneakyThrows
    @Override
    public void handle(StatusRequestPacket statusRequestPacket) {

        // If the state is not the STATUS one, close the connection and notify the problem
        if (this.state != InitialHandlerState.STATUS) {
            this.proxy.getLogger().warning("Not expecting status request packet during " + this.state + " state.");
            this.channelWrapper.close();
            return;
        }

        // Create the status response object
        ServerListPingResponse response = new ServerListPingResponse(new ServerListPingResponse.ServerListVersion("Hashcraft 1.0-SNAPSHOT", 47),
                new ServerListPingResponse.ServerListPlayers((Integer) ProxyConfiguration.MAX_PLAYERS_AMOUNT.get(), Proxy.getInstance().getPlayersCount(), null),
                new TextChatComponent(ProxyConfiguration.SERVER_MESSAGE_OF_THE_DAY.getStringWithColors()), null);

        // Write the packet
        this.channelWrapper.write(new StatusResponsePacket(gson.toJson(response)));

        // Change state to PING state
        this.state = InitialHandlerState.PING;
    }

    /**
     * This function will handle the status ping packet
     *
     * @param statusPingPacket The ping packet
     */
    @Override
    public void handle(StatusPingPacket statusPingPacket) {

        // If the state is not the PING one, close the connection and notify the problem
        if (this.state != InitialHandlerState.PING) {
            this.proxy.getLogger().warning("Not expecting ping packet during " + this.state + " state.");
            this.channelWrapper.close();
            return;
        }

        // Send the packet
        this.channelWrapper.write(statusPingPacket);

        // Close the channel after sending the packet
        this.channelWrapper.close();
    }

    /**
     * This function will handle the legacy ping packet.
     *
     * @param legacyPingPacket The legacy ping packet
     */
    @Override
    public void handle(LegacyPingPacket legacyPingPacket) {
        this.legacy = true;
        this.channelWrapper.close(); // Currently, not supported
    }

    /**
     * This function will handle the legacy handshake packet.
     *
     * @param legacyHandshakePacket The legacy handshake packet
     */
    @Override
    public void handle(LegacyHandshakePacket legacyHandshakePacket) {
        this.legacy = true;
        this.channelWrapper.close("Unsupported client"); // Version is too old
    }

    /**
     * This function will handle the login start packet
     *
     * @param loginStartPacket The login start packet
     */
    @Override
    public void handle(@NotNull LoginStartPacket loginStartPacket) {
        // Instance the LoginStartPacket value in this class
        this.loginStart = loginStartPacket;

        // If the initial handler is set to online mode, start the encryption procedure (Currently not developed)
        if (this.onlineMode) {

            // Send back a disconnect packet
            this.channelWrapper.write(new LoginDisconnectPacket(new TextChatComponent("§cOnline mode not supported!")));

            // Close the channel
            this.channelWrapper.close();
        }
        else { // Otherwise, jump to the finishLogin function
            this.finishLogin();
        }
    }

    /**
     * Internal function to finish the login process
     */
    private void finishLogin() {

        // If the connection is in onilne mode
        if (onlineMode) {
            // Close the channel & return
            this.channelWrapper.close();
            return;
        }

        // If a user with that username has already connected
        if (this.proxy.getPlayers().containsKey(this.loginStart.getName())) {
            // Send back a disconnect packet
            this.channelWrapper.write(new LoginDisconnectPacket(new TextChatComponent("§cThis username has already taken!")));

            // Close the channel and return
            this.channelWrapper.close();
            return;
        }

        // Register the new player
        final HashcraftPlayer hashcraftPlayer = new HashcraftPlayer(this.loginStart.getName(), UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.loginStart.getName()).getBytes(StandardCharsets.UTF_8)), this.channelWrapper);
        hashcraftPlayer.setCompression(256); // Default value

        // Send the login success packet
        this.channelWrapper.write(new LoginSuccessPacket(hashcraftPlayer.getUuid().toString(), this.loginStart.getName()));

        // Register the player in the proxy object
        this.proxy.getPlayers().put(this.loginStart.getName(), hashcraftPlayer);

        // Change the handler to the new one
        this.channelWrapper.getChannel().pipeline().get(HashcraftProxyHandler.class).setPacketHandler(new ToClientConnectionHandler(this.proxy.getPlayers().get(this.loginStart.getName())));

        // Notify that the user has joined the proxy
        this.proxy.getLogger().info(this.channelWrapper.getRemoteAddress() + " has joined with username: " +
                this.loginStart.getName());
    }

    /**
     * This function will set the channel wrapper into the AbstractPacketHandler. This function should be called by the
     * {@link com.github.lory24.hashcraft.proxy.netty.HashcraftProxyHandler} when the client connects.
     *
     * @param channelWrapper The channelWrapper obj
     */
    @Override
    public void connect(ChannelWrapper channelWrapper) {
        this.channelWrapper = channelWrapper; // Instance the ChannelWrapper obj
    }
}
