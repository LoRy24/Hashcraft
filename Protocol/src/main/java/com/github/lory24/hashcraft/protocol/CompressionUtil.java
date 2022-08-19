package com.github.lory24.hashcraft.protocol;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility to compress & decompress data
 */
public class CompressionUtil {

    /**
     * This function will compress or decompress data using zlib.
     *
     * @param compress If the data should be compressed or decompressed
     * @param input The input buffer
     * @param output The output buffer
     */
    public static void processData(final boolean compress, @NotNull final ByteBuf input, final ByteBuf output)
            throws DataFormatException {

        // Read the data into a byte array
        byte[] data = new byte[input.readableBytes()];
        input.readBytes(data);

        // Buffer object (I think this is exploited, hackers tell me your next move ;D)
        byte[] buffer = new byte[8096];

        // If it should compress the data
        if (compress) {

            // Set the input and finish
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.finish();

            // Compress the data
            while (!deflater.finished()) output.writeBytes(buffer, 0, deflater.deflate(buffer));

            // Reset the deflater
            deflater.reset();
        }
        else { // Decompress

            // Zip inflater obj. Set the data as input
            Inflater inflater = new Inflater();
            inflater.setInput(data);

            // Start decompressing the data
            while (!inflater.finished() && inflater.getTotalIn() < data.length) output.writeBytes(buffer, 0, inflater.inflate(buffer));

            // Reset the inflater
            inflater.reset();
        }
    }
}
