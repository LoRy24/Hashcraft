package com.github.lory24.hashcraft.chatcomponent;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The HoverEvent record. This will get the two params, and it will parse them into a new JSON string
 * @param hoverAction The action
 * @param value The value of the action.
 */
public record HoverEvent(ChatHoverAction hoverAction, String value) {

    @NotNull
    @Override
    public String toString() {
        return "\"hoverEvent\": {" +
                "\"action\":\"" + this.hoverAction.name().toLowerCase(Locale.ROOT) + "\"," +
                "\"value\":\"" + this.value + "\"" +
                "}";
    }
}
