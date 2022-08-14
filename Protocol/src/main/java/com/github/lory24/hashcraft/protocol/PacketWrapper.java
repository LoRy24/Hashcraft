package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Used by the netty to store the packet's data and the packet obj
 */
@RequiredArgsConstructor
public class PacketWrapper {

    /**
     * The Packet obj
     */
    @Getter
    private final Packet packet;

    /**
     * The buffer of the packet
     */
    @Getter
    private final ByteBuf buf;

    /**
     * If the packet is released
     */
    @Getter
    private boolean released;

    /**
     * Release the packet
     */
    public void release() {
        if (released) return; // If it's already released, return

        // If it's not, release the packet
        buf.release();
        released = true;
    }
}
