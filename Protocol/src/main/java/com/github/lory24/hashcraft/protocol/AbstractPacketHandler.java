package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.protocol.packet.*;
import com.github.lory24.hashcraft.protocol.packet.legacy.LegacyHandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.legacy.LegacyPingPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginStartPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusPingPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusRequestPacket;

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

    /**
     * This function will handle the login start packet
     *
     * @param loginStartPacket The login start packet
     */
    public void handle(final LoginStartPacket loginStartPacket) {}
}
