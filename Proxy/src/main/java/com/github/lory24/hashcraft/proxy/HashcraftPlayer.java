package com.github.lory24.hashcraft.proxy;

import com.github.lory24.hashcraft.api.ProxiedPlayer;
import com.github.lory24.hashcraft.protocol.packet.login.LoginSetCompressionPacket;
import com.github.lory24.hashcraft.proxy.netty.ChannelWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class HashcraftPlayer extends ProxiedPlayer {

    /**
     * The username of the player. Instanced when creating this object
     */
    private final String username;

    /**
     * The uuid in the proxy
     */
    private final UUID uuid;

    /**
     * The player's channel wrapper obj
     */
    @Getter
    private final ChannelWrapper channel;

    /**
     * Return the name of the player
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Update the compression threshold. This will also send to the client a setCompression packet.
     *
     * @param threshold The compression threshold.
     */
    public void setCompression(final int threshold) {

        // Set compression only if the threshold isn't negative
        if (threshold >= 0) {

            // Send the packet & enbable the compression on the channel wrapper
            this.channel.write(new LoginSetCompressionPacket(threshold));
            this.channel.setCompression(threshold);
        }
    }
}
