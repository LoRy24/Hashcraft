package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * The packet class. This contains all the features that a packet need and some other utility functions.
 * @author LoRy24
 */
public abstract class Packet {

    /**
     * Read a varint from a netty bytebuf
     *
     * @param buf The buffer from where this function should read the VarInt
     * @return The varInt
     */
    public static int readVarInt(@NotNull final ByteBuf buf) {
        // Define the current value and the current position integers
        int value = 0, position = 0;
        byte current; // The current byte

        while (true) {
            current = buf.readByte(); // Read the byte from the bytebuf
            value |= (current & 0x7f) << position; // Apply the value
            if ((current & 0x80) == 0) break; // Check if it's the last byte
            position += 7; // Increase the position by 7 bits
            if (position >= 32) throw new RuntimeException("VarInt is too big"); // Trow an exception if the value is too big
        }

        return value;
    }
}
