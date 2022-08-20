package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.api.Proxy;
import com.github.lory24.hashcraft.protocol.*;
import com.github.lory24.hashcraft.proxy.Hashcraft;
import com.github.lory24.hashcraft.proxy.handlers.InitialHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class HashcraftChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Hashcraft proxy reference
     */
    @Getter
    private final Hashcraft hashcraft;

    /**
     * Initialize the channel
     * @param socketChannel The future socket channel
     */
    @Override
    protected void initChannel(@NotNull SocketChannel socketChannel) {
        // Define a reference to the channelPipeline
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        // Add the legacy decoder
        channelPipeline.addLast("legacy-decoder", new MinecraftLegacyDecoder());

        // Add the frame decoder.
        channelPipeline.addLast("frame-decoder", new FrameDecoder());

        // Add the minecraft decoder
        channelPipeline.addLast("minecraft-decoder", new MinecraftPacketDecoder(ProtocolUtils.HANDSHAKE, true));

        // Add the frame encoder
        channelPipeline.addLast("frame-encoder", new FrameEncoder());

        // Add the minecraft encoder
        channelPipeline.addLast("minecraft-encoder", new MinecraftPacketEncoder(ProtocolUtils.HANDSHAKE, true));

        // Add the HashcraftProxyHandler and set the initial packetHandler
        channelPipeline.addLast("handler", new HashcraftProxyHandler());
        channelPipeline.get(HashcraftProxyHandler.class).setPacketHandler(new InitialHandler(this.hashcraft));

    }
}
