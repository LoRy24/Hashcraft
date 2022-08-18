package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;

/**
 * The packet class. This contains all the features that a packet need and some other utility functions.
 * @author LoRy24
 */
public abstract class Packet extends PacketUtils {

    /**
     * Read the data from a bytebuf into the packet object.
     *
     * @param buf The buf from where to read the data
     */
    public abstract void read(final ByteBuf buf);

    /**
     * Write the data stored in the packet object into a netty bytebuf
     *
     * @param buf The buf where to write the data
     */
    public abstract void write(final ByteBuf buf) throws Exception;

    /**
     * This function will be executed on the packet to handle it.
     *
     * @param packetHandler The current packet handler. Can be used to handle the packet in the current contex
     */
    public abstract void handle(final AbstractPacketHandler packetHandler) throws Exception;
}
