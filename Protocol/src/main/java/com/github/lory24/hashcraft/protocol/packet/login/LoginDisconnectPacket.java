package com.github.lory24.hashcraft.protocol.packet.login;

import com.github.lory24.hashcraft.chatcomponent.ChatComponent;
import com.github.lory24.hashcraft.protocol.AbstractPacketHandler;
import com.github.lory24.hashcraft.protocol.Packet;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginDisconnectPacket extends Packet {

    /**
     * The disconnect reason chat component
     */
    @Getter
    private ChatComponent reason;


    /**
     * Read the data from a bytebuf into the packet object.
     *
     * @param buf The buf from where to read the data
     */
    @Override
    public void read(ByteBuf buf) throws Exception {
        this.reason = readChatComponent(buf);
    }

    /**
     * Write the data stored in the packet object into a netty bytebuf
     *
     * @param buf The buf where to write the data
     */
    @Override
    public void write(ByteBuf buf) throws Exception {
        writeChatComponent(this.reason, buf);
    }

    /**
     * This function will be executed on the packet to handle it.
     *
     * @param packetHandler The current packet handler. Can be used to handle the packet in the current contex
     */
    @Override
    public void handle(AbstractPacketHandler packetHandler) throws Exception {

    }
}
