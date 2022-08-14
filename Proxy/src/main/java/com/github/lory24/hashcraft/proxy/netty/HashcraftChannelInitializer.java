package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.*;
import com.github.lory24.hashcraft.proxy.handlers.InitialHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.jetbrains.annotations.NotNull;

public class HashcraftChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Initialize the channel
     * @param socketChannel The future socket channel
     */
    @Override
    protected void initChannel(@NotNull SocketChannel socketChannel) throws Exception {
        // Define a reference to the channelPipeline
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        channelPipeline.addLast("frame-decoder", new FrameDecoder()); // Add the frame decoder.

        // Add the legacy decoder
        channelPipeline.addBefore("frame-decoder", "legacy-decoder", new MinecraftLegacyDecoder());

        // Add the minecraft decoder
        channelPipeline.addLast("minecraft-decoder", new MinecraftPacketDecoder(ProtocolUtils.HANDSHAKE, true));

        channelPipeline.addLast("frame-encoder", new FrameEncoder()); // Add the frame encoder

        // Add the minecraft encoder
        channelPipeline.addLast("minecraft-encoder", new MinecraftPacketEncoder(ProtocolUtils.HANDSHAKE, true));

        // Add the HashcraftProxyHandler and set the packetHandler
        channelPipeline.addLast("handler", new HashcraftProxyHandler());
        channelPipeline.get(HashcraftProxyHandler.class).setPacketHandler(new InitialHandler());

    }
}
