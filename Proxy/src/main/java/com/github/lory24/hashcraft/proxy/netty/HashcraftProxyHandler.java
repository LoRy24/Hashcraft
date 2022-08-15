package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.PacketWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * This class will handler everything that passes though the netty core.
 */
public class HashcraftProxyHandler extends ChannelInboundHandlerAdapter {

    /**
     * The packetHandler
     */
    @Setter
    private PacketHandler packetHandler;

    /**
     * The ChannelWrapper obj
     */
    @Getter
    private ChannelWrapper channelWrapper;

    /**
     * Read from the channel
     *
     * @param ctx The channel handler context
     * @param msg The message object (the packetWrapper)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // If the handler is not null, read
        if (packetHandler != null) {

            // Obtain the packetWrapper from the message parameter
            PacketWrapper packetWrapper = (PacketWrapper) msg;
            boolean shouldHandlePacket = packetHandler.shouldHandle(packetWrapper); // Check if it should handle the packet

            try {
                // Try to handle the packet if the packet isn't null, and it should handle it
                if (shouldHandlePacket && packetWrapper.getPacket() != null) {
                    packetWrapper.getPacket().handle(this.packetHandler);
                    return;
                }

                // Handle the packetBuffer
                this.packetHandler.handle(packetWrapper);
            }
            finally {
                packetWrapper.release(); // Release the packet
            }
        }
    }

    /**
     * When the channel is active, call the connect function in the packet handler
     *
     * @param ctx The channel handler context parameter
     */
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {

        // Call the connect function in the handler & instance the channel wrapper in this class obj
        this.channelWrapper = new ChannelWrapper(ctx.channel());
        this.packetHandler.connect(this.channelWrapper);
    }

    /**
     * When the channel is inactive (closed), call the
     *
     * @param ctx The channel handler context parameter
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        // Call the disconnect function in the packetHandler obj
        this.packetHandler.disconnect(this.channelWrapper);
    }
}
