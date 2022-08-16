package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.api.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The frame decoder is used by the netty to select the area of each packet.
 */
public class FrameDecoder extends ByteToMessageDecoder {

    /**
     * Decode (frame) the received data
     * @param channelHandlerContext           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param byteBuf            the {@link ByteBuf} from which to read data
     * @param list           the {@link List} to which decoded messages should be added
     */
    @Override
    protected void decode(@NotNull ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        // Try to create the packet frame
        try {
            // If the channel handler context is no more active, stop reading the data
            if (!channelHandlerContext.channel().isActive()) {
                // Skip all the readable bytes and return
                byteBuf.skipBytes(byteBuf.readableBytes());
                return;
            }

            // Mark the index
            byteBuf.markReaderIndex();

            // Read the length of the packet
            int length = PacketUtils.readVarInt(byteBuf);

            // Check if the length value is invalid and close the channel
            if (length <= 0 || byteBuf.readableBytes() < length) {
                Proxy.getInstance().getLogger().warning("Invalid packet length received from " + channelHandlerContext.channel().remoteAddress() + ": " + length);
                channelHandlerContext.channel().close();
                return; // Return
            }

            // Read the bytes
            ByteBuf dest = Unpooled.buffer(length);
            byteBuf.readBytes(dest);
            list.add(dest);
        } catch (Exception e) { // If an error occours, close the channel without any timeless
            Proxy.getInstance().getLogger().warning("An error has occourred when framing a packet received from " +
                    channelHandlerContext.channel().remoteAddress() +  ":" + e.getLocalizedMessage());
            channelHandlerContext.channel().close();
        }
    }
}
