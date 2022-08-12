package com.github.lory24.hashcraft.proxy.netty;

import com.github.lory24.hashcraft.protocol.FrameDecoder;
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

        channelPipeline.addLast("framer", new FrameDecoder()); // Add the framer
    }
}
