package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.AbstractPacketHandler;
import com.github.lory24.hashcraft.protocol.PacketWrapper;
import com.github.lory24.hashcraft.protocol.packet.HandshakePacket;

/**
 * This class will be exted by the packet handlers. The packet handlers are classes that are designed to do things
 * when a packet has been received by the proxy. With these functions, the handler will be able to process the packets
 * and do some other things.
 */
public class PacketHandler extends AbstractPacketHandler {

    /**
     * This function will return if the HashcraftProxyHandler should handle the packet
     */
    public boolean shouldHandle(PacketWrapper packetWrapper) {
        return true;
    }

    /**
     * This function will set the channel wrapper into the AbstractPacketHandler. This function should be called by the
     * HashcraftProxyHandler when the client connects.
     *
     * @param channelWrapper The channelWrapper obj
     */
    public void connect(final ChannelWrapper channelWrapper) {}

    /**
     * Mark the connection as disconnected
     *
     * @param channelWrapper The channelWrapper
     */
    public void disconnect(final ChannelWrapper channelWrapper) {}
}
