package com.github.lory24.hashcraft.chatcomponent;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The ClickEvent record. This will get the two params, and it will parse them into a new JSON string
 * @param clickAction The action
 * @param value The value of the action.
 */
public record ClickEvent(ChatClickAction clickAction, String value) {

    @NotNull
    @Override
    public String toString() {
        return "\"clickEvent\": {" +
                "\"action\":\"" + this.clickAction.name().toLowerCase(Locale.ROOT) + "\"," +
                "\"value\":\"" + this.value + "\"" +
                "}";
    }
}
