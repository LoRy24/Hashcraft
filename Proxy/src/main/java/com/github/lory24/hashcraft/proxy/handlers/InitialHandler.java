package com.github.lory24.hashcraft.proxy.handlers;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.protocol.packet.HandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.LegacyPingPacket;
import com.github.lory24.hashcraft.proxy.netty.ChannelWrapper;
import com.github.lory24.hashcraft.proxy.netty.PacketHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class InitialHandler extends PacketHandler {

    /**
     * The ChannelWrapper obj
     */
    @Getter
    private ChannelWrapper channelWrapper;

    /**
     * This function will handle the handshake packet.
     *
     * @param handshakePacket The handshake packet
     */
    @Override
    public void handle(@NotNull HandshakePacket handshakePacket) {
        // Notify the next state ID
        Proxy.getInstance().getLogger().info("Handshake packet received! NextState ID: " + handshakePacket.getNextState());
    }

    /**
     * This function will handle the handshake packet.
     *
     * @param legacyPingPacket The legacy ping packet
     */
    @Override
    public void handle(LegacyPingPacket legacyPingPacket) throws Exception {

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
