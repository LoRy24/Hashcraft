package com.github.lory24.hashcraft.proxy.utils;

/**
 * This enum will be used to define the state that the InitialHandler is processing.
 */
public enum InitialHandlerState {

    /**
     * This state will be used when the initial handler is processing the handshake state.
     */
    HANDSHAKE,

    /**
     * This state will be used when the initial handler is processing the status state
     */
    STATUS,

    /**
     * This state will be used by the InitialHandler when he is waiting for the ping packet during the status state
     */
    PING,
}
