package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.protocol.packet.LegacyHandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.LegacyPingPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * When Minecraft can't follow the normal procedures, it will try to follow them in a legacy way. For example, if the
 * status process break itself (maybe with some formatting errors), Minecraft will try to send a LegacyPing request. Another
 * example is the LegacyHandshake, that occours when the normal handshake procedure goes wrong. A Minecfraft server should
 * be free to implement or not this feature, but modern server should always implement this. So, with this class, the proxy
 * is going to add this system into his core.
 *
 * @author LoRy24
 */
public class MinecraftLegacyDecoder extends ByteToMessageDecoder {

    /**
     * Decode the received data
     *
     * @param channelHandlerContext The current chanel handler
     * @param byteBuf The data
     * @param list The output list
     */
    @Override
    protected void decode(@NotNull ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        // If the channel isn't active, return
        if (!channelHandlerContext.channel().isActive()) {
            byteBuf.skipBytes(byteBuf.readableBytes()); // Skip all the bytes
            return;
        }

        // If the buffer isn't readable, return without doing anything
        if (!byteBuf.isReadable()) return;

        // Mark the reader index and read the packet id (it's an unsigned byte, legacy id is written with this data type)
        byteBuf.markReaderIndex();
        short packetID = byteBuf.readUnsignedByte();

        // If the packetID is 0xFE, it's a LegacyPing request. Else if it's 0x02, it's a legacy handshake
        if (packetID == 0xFE) {
            // Process the legacy ping
            list.add(new PacketWrapper(new LegacyPingPacket(byteBuf.isReadable() && byteBuf.readUnsignedByte() == 0x01), Unpooled.EMPTY_BUFFER));
            return;
        }
        else if (packetID == 0x02 && byteBuf.isReadable()) {
            // Process the legacy handshake
            list.add(new PacketWrapper(new LegacyHandshakePacket(), Unpooled.EMPTY_BUFFER));
            return;
        }

        // Reset the reader index
        byteBuf.resetReaderIndex();

        // Remove this Decoder from the netty pipeline
        channelHandlerContext.pipeline().remove(this);
    }
}
