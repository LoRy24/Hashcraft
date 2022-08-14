package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

public class FrameEncoder extends MessageToByteEncoder<ByteBuf> {

    /**
     * Encode the frame
     *
     * @param ctx           the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg           the message to encode
     * @param out           the {@link ByteBuf} into which the encoded message will be written
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, @NotNull ByteBuf msg, @NotNull ByteBuf out) {

        // Check function's documentation for more details
        out.ensureWritable(msg.readableBytes() + getVarIntSize(msg.readableBytes()));

        // Write the data
        PacketUtils.writeVarInt(msg.readableBytes(), out);
        out.writeBytes(msg);
    }

    /**
     * Get the size of a varInt number
     *
     * @param number The number to get the size
     * @return The amount of bytes required by the number
     */
    private int getVarIntSize(int number) {
        // Calculate the size of the varint
        return (number & 0xFFFFFF80) == 0 ? 1 : (number & 0xFFFFC000) == 0 ? 2 : (number & 0xFFE00000) == 0 ? 3 : (number & 0xF0000000) == 0 ? 4 : 5;
    }
}
