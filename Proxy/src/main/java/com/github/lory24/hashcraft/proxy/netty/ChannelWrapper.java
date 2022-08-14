package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.MinecraftPacketDecoder;
import com.github.lory24.hashcraft.protocol.MinecraftPacketEncoder;
import com.github.lory24.hashcraft.protocol.PacketWrapper;
import com.github.lory24.hashcraft.protocol.ProtocolUtils;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class ChannelWrapper {

    /**
     * The channel obj
     */
    private final Channel channel;

    /**
     * The remoteAddress
     */
    @Getter
    @Setter
    private SocketAddress remoteAddress;

    /**
     * If the channel is closed
     */
    @Getter
    private boolean closed;

    /**
     * The constructor for the ChannelWrapper obj
     *
     * @param channel The channel that this wrapper owns
     */
    public ChannelWrapper(@NotNull Channel channel) {
        this.channel = channel;
        this.remoteAddress = channel.remoteAddress(); // Set the default remote address value
    }

    /**
     * Update the connection protocol
     */
    public void updateProtocolUtils(ProtocolUtils protocol) {
        // Update the protocolUtils
        this.channel.pipeline().get(MinecraftPacketDecoder.class).setProtocolUtils(protocol);
        this.channel.pipeline().get(MinecraftPacketEncoder.class).setProtocolUtils(protocol);
    }

    /**
     * This function will write a packet from the server to the client
     *
     * @param packet The packet that should be sent
     */
    public void write(Object packet) {

        // If the channel is closed, return
        if (closed) return;

        // If the packet is an instance of PacketWrapper, send the packet obj
        if (packet instanceof PacketWrapper) {
            ((PacketWrapper) packet).release(); // Release the packet
            this.channel.writeAndFlush(((PacketWrapper) packet).getBuf(), channel.voidPromise());
            return;
        }

        // If it's not, write and flush
        this.channel.writeAndFlush(packet, channel.voidPromise());
    }
}
