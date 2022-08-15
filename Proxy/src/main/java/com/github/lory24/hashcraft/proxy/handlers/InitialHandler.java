package com.github.lory24.hashcraft.proxy.handlers;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.protocol.ProtocolUtils;
import com.github.lory24.hashcraft.protocol.packet.HandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.LegacyHandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.LegacyPingPacket;
import com.github.lory24.hashcraft.proxy.netty.ChannelWrapper;
import com.github.lory24.hashcraft.proxy.netty.PacketHandler;
import com.github.lory24.hashcraft.proxy.utils.InitialHandlerState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class InitialHandler extends PacketHandler {

    /**
     * An instance of the proxy obj
     */
    @Getter
    private final Proxy proxy;

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
            case 1: {
                // Notify if enabled
                if (this.proxy.getProxyConfiguration().shouldSendPingNotifications()) this.getProxy().getLogger().info(this.channelWrapper.getRemoteAddress() + " has pinged.");
                this.setState(InitialHandlerState.STATUS);
                this.channelWrapper.updateProtocolUtils(ProtocolUtils.STATUS);
                this.channelWrapper.close(); // Close the channel for now
                break;
            }

            // Login
            case 2: {

            }

            // Not expected: Close the channel and notify it
            default: {
                this.channelWrapper.close();
                this.proxy.getLogger().warning("Received an invalid NextState id: " + this.handshake.getNextState());
                break;
            }
        }
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
