package com.github.lory24.hashcraft.proxy.utils.netty;

import com.github.lory24.hashcraft.protocol.*;
import com.github.lory24.hashcraft.proxy.netty.HashcraftProxyHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.jetbrains.annotations.NotNull;

public class BaseChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(@NotNull SocketChannel socketChannel) throws Exception {
        // Define a reference to the channelPipeline
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        // Add the netty most important features
        channelPipeline
                .addLast(PipelineItems.FRAME_DECODER.toString(), new FrameDecoder())
                .addLast(PipelineItems.FRAME_ENCODER.toString(), new FrameEncoder())
                .addLast(PipelineItems.BOSS_HANDLER.toString(),  new HashcraftProxyHandler());
    }
}
