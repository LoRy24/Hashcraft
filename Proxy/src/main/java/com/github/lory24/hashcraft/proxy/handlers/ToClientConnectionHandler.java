package com.github.lory24.hashcraft.proxy.handlers;

import com.github.lory24.hashcraft.protocol.PacketWrapper;
import com.github.lory24.hashcraft.proxy.Hashcraft;
import com.github.lory24.hashcraft.proxy.HashcraftPlayer;
import com.github.lory24.hashcraft.proxy.netty.ChannelWrapper;
import com.github.lory24.hashcraft.proxy.netty.PacketHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ToClientConnectionHandler extends PacketHandler {

    /**
     * Reference to the player's object
     */
    @Getter
    private HashcraftPlayer player;

    /**
     * Mark the connection as disconnected
     *
     * @param channelWrapper The channelWrapper
     */
    @Override
    public void disconnect(ChannelWrapper channelWrapper) {
        // Remove the player from the proxy
        ((Hashcraft) Hashcraft.getInstance()).getPlayers().remove(player.getUsername());

        // Notify that the player has disconnected
        Hashcraft.getInstance().getLogger().info("User " + player.getUsername() + " (" + player.getChannel().getRemoteAddress() + ") has disconnected form the proxy!");
    }


    /**
     * This function will handle a PacketWrapper
     *
     * @param packetWrapper The packet wrapper to handle
     */
    @Override
    public void handle(PacketWrapper packetWrapper) {
        // Should send the packetWrapper's buffer to the sub server if it's ready
    }
}
