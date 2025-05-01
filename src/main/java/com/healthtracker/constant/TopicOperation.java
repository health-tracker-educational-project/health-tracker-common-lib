package com.healthtracker.constant;

import lombok.Getter;

@Getter
public enum TopicOperation {

    REQUEST("request"),
    RESPONSE("response");

    private final String name;

    TopicOperation(String name) {
        this.name = name;
    }

}
