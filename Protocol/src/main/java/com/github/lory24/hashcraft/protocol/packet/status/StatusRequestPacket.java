package com.github.lory24.hashcraft.protocol.packet.status;

import com.github.lory24.hashcraft.protocol.AbstractPacketHandler;
import com.github.lory24.hashcraft.protocol.Packet;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class StatusRequestPacket extends Packet {

    /**
     * Read the data from a bytebuf into the packet object.
     *
     * @param buf The buf from where to read the data
     */
    @Override
    public void read(ByteBuf buf) {
        // Nothing to read...
    }

    /**
     * Write the data stored in the packet object into a netty bytebuf
     *
     * @param buf The buf where to write the data
     */
    @Override
    public void write(ByteBuf buf) {
        // Nothing to write...
    }

    /**
     * This function will be executed on the packet to handle it.
     *
     * @param packetHandler The current packet handler. Can be used to handle the packet in the current contex
     */
    @Override
    public void handle(@NotNull AbstractPacketHandler packetHandler) throws Exception {

        // Handle the packet with the packetHandler
        packetHandler.handle(this);
    }
}
