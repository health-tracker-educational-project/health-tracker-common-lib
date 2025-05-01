package com.healthtracker.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TopicUtils {

    private static final String TOPICS_PROPERTY_PREFIX = "kafka.topics";

    public static String buildTopicPropertyPath(String entity, String action, String operation) {
        return TOPICS_PROPERTY_PREFIX + "." + entity.toLowerCase() + "." + action.toLowerCase() + "-" + operation.toLowerCase();
    }

}
