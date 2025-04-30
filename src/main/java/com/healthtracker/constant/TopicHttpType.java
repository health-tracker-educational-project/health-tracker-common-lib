package com.healthtracker.constant;

import lombok.Getter;

@Getter
public enum TopicHttpType {

    REQUEST("request"),
    RESPONSE("response");

    private final String name;

    TopicHttpType(String name) {
        this.name = name;
    }

}
