package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
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

        // If the channel handler context is no more active, stop reading the data
        if (!channelHandlerContext.channel().isActive()) {
            // Skip all the readable bytes and return
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        // Mark the index
        byteBuf.markReaderIndex();

        // Mini buffer of 3 bytes
        final byte[] buf = new byte[3];
        for (int i = 0; i < buf.length; i++) { // Start filling the buf & do all the framer checks

            // If the buf is not readable reset the index and return
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }

            buf[i] = byteBuf.readByte(); // Read the byte into the buf

            // If the byte is not negative
            if (buf[i] >= 0) {
                // Read the length of the packet
                int length = PacketUtils.readVarInt(Unpooled.wrappedBuffer(buf));

                // If the packet is empty
                if (length <= 0) {
                    throw new CorruptedFrameException("Invalid packet size! Must be > 0");
                }

                // If the length and the readableBytes aren't the same, it will return
                if (byteBuf.readableBytes() < length) {
                    byteBuf.resetReaderIndex(); // Reset the index
                    return;
                }

                // If the byteBuf has a memory address, copy the packet bytes and put it in the list of this function
                if (byteBuf.hasMemoryAddress()) {
                    list.add(byteBuf.slice(byteBuf.readerIndex(), length).retain());
                    return;
                }

                // Fix a sussy bug that has occoured in bungeecord
                ByteBuf dst = channelHandlerContext.alloc()
                        .directBuffer(length);
                byteBuf.readBytes(dst);
                list.add(dst);
                return;
            }
        }

        // The packet is too big
        throw new CorruptedFrameException("Packet data is too big.");
    }
}
