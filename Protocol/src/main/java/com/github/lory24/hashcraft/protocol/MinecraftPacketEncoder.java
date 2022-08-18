package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class MinecraftPacketEncoder extends MessageToByteEncoder<Packet> {

    /**
     * The protocol utils
     */
    @Setter
    private ProtocolUtils protocolUtils;

    /**
     * If the packet is fromServer
     */
    private boolean fromServer;

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, @NotNull Packet msg, ByteBuf out) {
        try {
            // Get the direction of the packet
            ProtocolUtils.DirectionalPackets protocolPacketDirection = fromServer ? protocolUtils.getToClient() : protocolUtils.getToServer();

            // Get the ID of the packet and if it's -1, return
            int packetID = protocolPacketDirection.getPacketID(msg.getClass());
            if (packetID == -1) return;

            // Write the packet
            PacketUtils.writeVarInt(packetID, out);
            msg.write(out); // Write the packet's data into the output
        } catch (Exception e) {
            e.printStackTrace();
            out.skipBytes(out.readableBytes());
        }
    }
}
