package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.*;
import com.github.lory24.hashcraft.proxy.Hashcraft;
import com.github.lory24.hashcraft.proxy.handlers.InitialHandler;
import com.github.lory24.hashcraft.proxy.utils.NettyPipelineUtilities;
import com.github.lory24.hashcraft.proxy.utils.netty.PipelineItems;
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
    protected void initChannel(@NotNull SocketChannel socketChannel) throws Exception {
        // Define a reference to the channelPipeline
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        // Initialize standard minecraft pipeline
        NettyPipelineUtilities.baseChannelInitialize.initChannel(socketChannel);

        // Add the legacy decoder
        channelPipeline.addBefore(PipelineItems.FRAME_DECODER.toString(), PipelineItems.LEGACY_DECODER.toString(), new MinecraftLegacyDecoder());

        // Add the minecraft decoder
        channelPipeline.addAfter(PipelineItems.FRAME_DECODER.toString(), PipelineItems.MINECRAFT_DECODER.toString(), new MinecraftPacketDecoder(ProtocolUtils.HANDSHAKE, true));

        // Add the minecraft encoder
        channelPipeline.addAfter(PipelineItems.FRAME_ENCODER.toString(), PipelineItems.MINECRAFT_ENCODER.toString(), new MinecraftPacketEncoder(ProtocolUtils.HANDSHAKE, true));

        // Set the initial handler
        channelPipeline.get(HashcraftProxyHandler.class).setPacketHandler(new InitialHandler(this.hashcraft));

    }
}
