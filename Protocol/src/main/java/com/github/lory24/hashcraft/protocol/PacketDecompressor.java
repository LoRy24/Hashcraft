package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * This will be added after the frame decoder. This will decode the compressed packet and make it readable to the
 * minecraft decoder.
 */
public class PacketDecompressor extends MessageToMessageDecoder<ByteBuf> {

    /**
     * Decode the compressed packet
     *
     * @param channelHandlerContext The channel context handler
     * @param buf The input buffer
     * @param list The output
     * @throws Exception If an exception occurs
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        // Read the data length
        int dataLength = PacketUtils.readVarInt(buf);

        // If the size is 0, the compression is not used
        if (dataLength == 0) {
            list.add(buf.retain());
        }
        else { // The packet is compressed
            // Allocate the buffer for the decompressed data
            ByteBuf decompressed = channelHandlerContext.alloc().directBuffer();

            // After decompressing the data, deallocate the buffer
            try {

                // Decompress the data
                CompressionUtil.processData(false, buf, decompressed);

                // Check if the size of the decompressed data and the red size are the same
                if (decompressed.readableBytes() != dataLength) throw new PacketCompressionException("The written packet's data size isn't equal to the decompressed one.");

                // Add the decompressed buffer to the output
                list.add(decompressed);

                // Empty the decompressed data buf
                decompressed = null;
            }
            finally {
                // If the buffer isn't empty, release it
                if (decompressed != null) decompressed.release();
            }
        }
    }
}
