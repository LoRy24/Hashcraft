package com.github.lory24.hashcraft.protocol.packet.status;

import com.github.lory24.hashcraft.protocol.AbstractPacketHandler;
import com.github.lory24.hashcraft.protocol.Packet;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class StatusPingPacket extends Packet {

    /**
     * The payload
     */
    private long payload;

    /**
     * Read the data from a bytebuf into the packet object.
     *
     * @param buf The buf from where to read the data
     */
    @Override
    public void read(@NotNull ByteBuf buf) {
        this.payload = buf.readLong();
    }

    /**
     * Write the data stored in the packet object into a netty bytebuf
     *
     * @param buf The buf where to write the data
     */
    @Override
    public void write(@NotNull ByteBuf buf) throws Exception {
        buf.writeLong(this.payload);
    }

    /**
     * This function will be executed on the packet to handle it.
     *
     * @param packetHandler The current packet handler. Can be used to handle the packet in the current contex
     */
    @Override
    public void handle(@NotNull AbstractPacketHandler packetHandler) throws Exception {
        // Handle with the handler
        packetHandler.handle(this);
    }
}
