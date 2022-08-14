package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.protocol.packet.HandshakePacket;

/**
 * The abstract packet handler. Contains function to handle each packet
 */
public abstract class AbstractPacketHandler {

    /**
     * This function will handle the handshake packet.
     *
     * @param handshakePacket The handshake packet
     */
    public void handle(final HandshakePacket handshakePacket) throws Exception {}
}
