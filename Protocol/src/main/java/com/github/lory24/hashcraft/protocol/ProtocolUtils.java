package com.github.lory24.hashcraft.protocol;

import com.github.lory24.hashcraft.protocol.packet.HandshakePacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginDisconnectPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginSetCompressionPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginStartPacket;
import com.github.lory24.hashcraft.protocol.packet.login.LoginSuccessPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusPingPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusRequestPacket;
import com.github.lory24.hashcraft.protocol.packet.status.StatusResponsePacket;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public enum ProtocolUtils {

    /**
     * Handshake packet initialization
     */
    HANDSHAKE
    {
        {
            getToServer().registerPacket(HandshakePacket.class);
        }
    },

    STATUS
    {
        {
            /*
             * TO SERVER
             */

            // Status request packet
            getToServer().registerPacket(StatusRequestPacket.class);
            getToServer().registerPacket(StatusPingPacket.class);

            /*
             * TO CLIENT
             */
            getToClient().registerPacket(StatusResponsePacket.class);
            getToClient().registerPacket(StatusPingPacket.class);
        }
    },

    LOGIN
    {
        {
            /*
             * TO SERVER
             */
            getToServer().registerPacket(LoginStartPacket.class);
            getToServer().registerPacket(null); // Encryption response not supported

            /*
             * TO CLIENT
             */
            getToClient().registerPacket(LoginDisconnectPacket.class);
            getToClient().registerPacket(null); // Encryption request not supported
            getToClient().registerPacket(LoginSuccessPacket.class);
            getToClient().registerPacket(LoginSetCompressionPacket.class);
        }
    },

    GAME_PROXY_TO_CLIENT
    {
        {

        }
    },
    ;

    // The packets directions

    @Getter
    private final DirectionalPackets toServer = new DirectionalPackets();

    @Getter
    private final DirectionalPackets toClient = new DirectionalPackets();


    /**
     * This class will be used to register all the packets by the state of the decoder & the encoder
     */
    public static final class DirectionalPackets {

        /**
         * The registered packets linked list
         */
        @Getter
        private final LinkedList<Class<? extends Packet>> registeredPackets = new LinkedList<>();

        /**
         * Register a packet in the context
         *
         * @param packetClass The class of the packet that should be registered
         */
        public void registerPacket(final Class<? extends Packet> packetClass) {
            // Register the packet class
            this.registeredPackets.add(packetClass);
        }

        /**
         * Create a packet from its id
         *
         * @param id The id of the packet that the function will create
         */
        @Nullable
        @Contract(pure = true)
        public Packet createPacket(final int id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

            // Return the packet initialized by his empty constructor
            try {
                return this.registeredPackets.get(id).getConstructor().newInstance();
            }
            catch (NullPointerException | IndexOutOfBoundsException ignored) {
                // If the packet doesn't exist, return null
                return null;
            }
        }

        /**
         * Return the ID of a packet by its class
         *
         * @param packetClass The class of the packet
         */
        public int getPacketID(final Class<? extends Packet> packetClass) {

            // Get the index of the packet's class
            return this.registeredPackets.indexOf(packetClass);
        }
    }
}
