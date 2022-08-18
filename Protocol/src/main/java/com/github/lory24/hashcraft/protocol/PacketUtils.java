package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.chatcomponent.ChatComponent;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Some utils for writing and reading data form a netty bytebuf
 */
public class PacketUtils {

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

    /**
     * Write a varint value into the netty buffer.
     *
     * @param value The value that should be written in the buffer
     * @param byteBuf The buffer where to put the data
     */
    public static void writeVarInt(int value, final ByteBuf byteBuf) {

        // "Infinite" loop
        while (true) {

            // Check if it is the last byte to write
            if ((value & ~0x7F) == 0) {
                byteBuf.writeByte(value);
                return;
            }

            // Write the byte
            byteBuf.writeByte((value & 0x7F) | 0x80);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    /**
     * Write a string into a buffer.
     *
     * @param byteBuf Where to write the string
     * @param value The string to write in the buffer
     */
    public static void writeString(@NotNull String value, final ByteBuf byteBuf) throws Exception {

        // Check if the string is too long
        if (value.getBytes(StandardCharsets.UTF_8).length > 32767) throw new Exception("String is too long!");

        // Write the size of the string
        writeVarInt(value.getBytes(StandardCharsets.UTF_8).length, byteBuf);

        // Write the string bytes
        byteBuf.writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Read a string from a buffer
     *
     * @param buf The buffer from where to read the string
     * @return The string
     */
    @NotNull
    @Contract("_ -> new")
    public static String readString(final ByteBuf buf) {
        // Read the size of the string
        int stringSize = readVarInt(buf);

        // Read the string bytes
        byte[] stringBytes = new byte[stringSize];
        buf.readBytes(stringBytes);

        // Return the new string
        return new String(stringBytes, StandardCharsets.UTF_8);
    }
}
