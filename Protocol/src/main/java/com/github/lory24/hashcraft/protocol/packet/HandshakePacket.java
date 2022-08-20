package com.github.lory24.hashcraft.protocol.packet;

import com.github.lory24.hashcraft.protocol.AbstractPacketHandler;
import com.github.lory24.hashcraft.protocol.Packet;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class HandshakePacket extends Packet {

    // See https://wiki.vg/index.php?title=Protocol&oldid=7368#Handshake for more details

    @Getter
    private int protocolVersion;
    @Getter
    private String serverAddress;
    @Getter
    private int serverPort;
    @Getter
    private int nextState;

    /**
     * Read the data from a bytebuf into the packet object.
     *
     * @param buf The buf from where to read the data
     */
    @Override
    public void read(ByteBuf buf) {
        // Read all the packet's data
        this.protocolVersion = readVarInt(buf);
        this.serverAddress = readString(buf);
        this.serverPort = buf.readUnsignedShort();
        this.nextState = readVarInt(buf);
    }

    /**
     * Write the data stored in the packet object into a netty bytebuf
     *
     * @param buf The buf where to write the data
     */
    @SneakyThrows
    @Override
    public void write(ByteBuf buf) {
        // Write the packet's data
        writeVarInt(this.protocolVersion, buf);
        writeString(this.serverAddress, buf);
        buf.writeShort(this.serverPort);
        writeVarInt(this.nextState, buf);
    }

    /**
     * This function will be executed on the packet to handle it.
     *
     * @param packetHandler The current packet handler. Can be used to handle the packet in the current contex
     */
    @SneakyThrows
    @Override
    public void handle(@NotNull AbstractPacketHandler packetHandler) {
        // Handle the packet
        packetHandler.handle(this);
    }
}
