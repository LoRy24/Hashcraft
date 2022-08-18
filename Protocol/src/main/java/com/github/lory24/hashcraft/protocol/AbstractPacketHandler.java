package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.protocol.packet.*;

/**
 * The abstract packet handler. Contains function to handle each packet
 */
@SuppressWarnings("RedundantThrows")
public abstract class AbstractPacketHandler {

    /**
     * This function will handle the handshake packet.
     *
     * @param handshakePacket The handshake packet
     */
    public void handle(final HandshakePacket handshakePacket) throws Exception {}

    /**
     * This function will handle the legacy ping packet.
     *
     * @param legacyPingPacket The legacy ping packet
     */
    public void handle(final LegacyPingPacket legacyPingPacket) throws Exception {}

    /**
     * This function will handle the legacy handshake packet.
     *
     * @param legacyHandshakePacket The legacy handshake packet
     */
    public void handle(final LegacyHandshakePacket legacyHandshakePacket) throws Exception {}

    /**
     * This function will handle the status request packet
     *
     * @param statusRequestPacket The request packet
     */
    public void handle(final StatusRequestPacket statusRequestPacket) throws Exception {}

    /**
     * This function will handle the status ping packet
     *
     * @param statusPingPacket The ping packet
     */
    public void handle(final StatusPingPacket statusPingPacket) {}
}
