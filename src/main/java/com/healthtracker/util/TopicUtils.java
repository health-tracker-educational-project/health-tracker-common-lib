package com.healthtracker.util;

public class TopicUtils {

    private static final String TOPICS_PROPERTY_PREFIX = "kafka.topics";

    public static String buildTopicPropertyPath(String entity, String action, String httpType) {
        return TOPICS_PROPERTY_PREFIX + "." + entity.toLowerCase() + "." + action.toLowerCase() + "-" + httpType.toLowerCase();
    }

}
