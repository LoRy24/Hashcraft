package com.github.lory24.hashcraft.chatcomponent;

/**
 * A special ChatComponent designed to write only a text field in the constructor.
 */
public class TextChatComponent extends ChatComponent {

    /**
     * The constructor for the TextChatComponent class.
     * @param text The only parameter of the TextChatComponent. The ChatComponent will start only with this
     */
    public TextChatComponent(final String text) {
        this.text = text;
    }

    /**
     * Build the text chat component into a new json string
     * @return The json string
     */
    public String buildTextChatComponent() {
        return this.toJson().replace("§", "\\u00a7");
    }
}
