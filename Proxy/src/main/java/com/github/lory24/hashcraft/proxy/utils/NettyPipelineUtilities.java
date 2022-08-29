package com.github.lory24.hashcraft.proxy.utils;

import com.github.lory24.hashcraft.proxy.utils.netty.BaseChannelInitializer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NettyPipelineUtilities {

    /**
     * This object will be used to implement the basical features in a netty channel
     */
    public static final BaseChannelInitializer baseChannelInitialize = new BaseChannelInitializer();
}
