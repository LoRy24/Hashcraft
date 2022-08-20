package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class ChannelWrapper {

    /**
     * The channel obj
     */
    @Getter
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

    /**
     * Close the channel without sending anything
     */
    public void close() {
        this.close(null); // Close without sending
    }

    /**
     * This function will close the channel sending back an obj
     *
     * @param packet The object to send back
     */
    public void close(Object packet) {

        // If the channel is closed, return
        if (closed) return;

        // Mark as closed
        closed = true;

        // If the packet isn't null and the channel is active, write the data
        if (packet != null && this.channel.isActive()) {
            this.channel.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE, ChannelFutureListener.CLOSE);
            return;
        }

        this.channel.flush().close(); // Flush and close the channel
    }

    /**
     * This function will be used to set up the compression system. This function will be called when finishing the login
     * state, so this function will also send the set-compression packet.
     *
     * @param threshold The threshold number.
     */
    public void setCompression(final int threshold) {

        // If the PacketDecompressor isn't in the pipeline, and the threshold isn't -1
        if (this.channel.pipeline().get(PacketDecompressor.class) == null && threshold != -1) {
            // Flush and add the packet decompressor to the pipeline
            this.channel.pipeline().flush().addBefore("minecraft-decoder", "packet-decompressor", new PacketDecompressor());
        }

        // If the PacketCompressor isn't in the pipeline, and the threshold isn't -1
        if (this.channel.pipeline().get(PacketCompressor.class) == null && threshold != -1) {
            // Flush and add the packet compressor to the pipeline
            this.channel.pipeline().flush().addBefore("minecraft-encoder", "packet-compressor", new PacketCompressor());
        }

        // If the threshold is not negative, set the compression threshold
        if (threshold != -1) {
            this.channel.pipeline().get(PacketCompressor.class).setThreshold(threshold);
        }
    }
}
