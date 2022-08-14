package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class MinecraftPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * The protocol utils obj
     */
    @Setter
    private ProtocolUtils protocolUtils;

    /**
     * If the packets' direction are client -> server
     */
    boolean isToServer;

    /**
     * Decode from one message to another. This method will be called for each written message that can be handled
     * by this decoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageDecoder} belongs to
     * @param msg the message to decode to another one
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(@NotNull ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        // If the channel isn't active, return
        if (!ctx.channel().isActive()) return;

        // Set up the protocol utils. Used to create the packets
        ProtocolUtils.DirectionalPackets protocolDirectionalPackets = isToServer ? protocolUtils.getToServer() : protocolUtils.getToClient();

        // Slice the buffer
        ByteBuf slice = msg.copy();

        try {
            // Read the packet's id
            int packetId = Packet.readVarInt(msg);

            // Create the packet
            Packet packet = protocolDirectionalPackets.createPacket(packetId);

            // If the packet isn't null
            if (packet != null) {

                // Make the packet read the data
                packet.read(msg);

                // If there are more bytes, throw an exception
                if (msg.isReadable()) {
                    throw new MalformedPacketException("Malformed packet: The received data was larger than expected!");
                }
            }
            else { // Skip the packet's bytes
                msg.skipBytes(msg.readableBytes());
            }

            // Add the packet wrapper into the output & clear the slice
            out.add(new PacketWrapper(packet, slice));
            slice = null;
        }
        finally {
            // Try to release the slice
            if (slice != null) slice.release();
        }
    }
}
