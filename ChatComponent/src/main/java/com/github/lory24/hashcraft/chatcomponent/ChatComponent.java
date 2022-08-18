package com.github.lory24.hashcraft.chatcomponent;

import com.google.gson.Gson;
import org.jetbrains.annotations.Contract;

/**
 * The ChatComponent is an object designed to send custom messages over the Minecraft network. When it's serialized, it's
 * written as a JSON. In order to do that, this class will use the Google JSON api "Gson".
 *
 * @author LoRy24
 */
@SuppressWarnings("unused")
public abstract class ChatComponent {
    public String text = "";
    public boolean bold;
    public boolean italic;
    public boolean underlined;
    public boolean strikethrough;
    public boolean obfuscated;
    public String font = "minecraft:default";
    public String color;
    public String insertion;
    public ClickEvent clickEvent;
    public HoverEvent hoverEvent;
    public TextChatComponent[] extras;

    /**
     * Put all the stuff into a JSON string
     * @return The json
     */
    @SuppressWarnings("ConstantConditions")
    public String toJson() {
        return new Gson().toJson(this).replace("&", "\u00a7").replace("§", "\u00a7");
    }

    /**
     * Create a new ChatComponent from a json string
     * @param json The json string
     * @return The Gson object
     */
    @Contract(pure = true)
    public static ChatComponent fromJson(String json) {
        return new Gson().fromJson(json, ChatComponent.class);
    }
}
