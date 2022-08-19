package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    /**
     * This value indicate the amount of bytes that if a packet exceeds, it should compress his data.
     */
    @Setter
    private int threshold = 256;

    /**
     * Compress the data.
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, @NotNull ByteBuf buf, ByteBuf byteBuf) throws Exception {

        // If the bytes exceed the threshold
        if (buf.readableBytes() > this.threshold) {

            // Compress the data and send it
            PacketUtils.writeVarInt(buf.readableBytes(), byteBuf);

            // Write the compressed bytes in the output and return
            CompressionUtil.processData(true, buf, byteBuf);
            return;
        }

        // Send the value 0: No compression applied
        PacketUtils.writeVarInt(0, byteBuf);

        // Write the data
        byteBuf.writeBytes(buf);
    }
}
