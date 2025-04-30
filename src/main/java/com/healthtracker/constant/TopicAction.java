package com.healthtracker.constant;

import lombok.Getter;

@Getter
public enum TopicAction {
    LIST("list", true),
    SAVE("save", true),
    GET("get", true),
    EDIT("edit", true),
    DELETE("delete", false);

    private final String prefix;
    private final boolean responseRequired;

    TopicAction(String prefix, boolean responseRequired) {
        this.prefix = prefix;
        this.responseRequired = responseRequired;
    }
}
