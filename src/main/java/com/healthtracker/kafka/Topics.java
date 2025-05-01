package com.healthtracker.kafka;

import com.healthtracker.constant.TopicAction;
import com.healthtracker.constant.TopicOperation;
import com.healthtracker.util.TopicUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Topics {

    private final List<String> topicNames;

    @Value("${kafka.dlt-topic-suffix}")
    private String dltSuffix;

    public Topics(TopicEntities topicEntities, ConfigurableEnvironment environment) {
        Map<String, Object> topics = topicEntities.getEntities().stream()
                .flatMap(Topics::buildActions)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        this.topicNames = topics.values().stream()
                .map(String::valueOf)
                .toList();

        environment.getPropertySources()
                .addFirst(new MapPropertySource("topics", topics));
    }

    public List<String> getTopics() {
        return Stream.concat(
                this.topicNames.stream(),
                this.topicNames.stream().map(topic -> topic + dltSuffix)
        ).toList();
    }

    private static Stream<Map.Entry<String, String>> buildActions(String entity) {
        return Arrays.stream(TopicAction.values())
                .flatMap(action -> buildOperations(entity, action));
    }

    private static Stream<Map.Entry<String, String>> buildOperations(String entity, TopicAction action) {
        return Stream.concat(
                Stream.of(buildTopicNamesMap(action.getPrefix(), entity, TopicOperation.REQUEST)),
                Stream.ofNullable(action.isResponseRequired() ? buildTopicNamesMap(action.getPrefix(), entity, TopicOperation.RESPONSE) : null)
        );
    }

    private static Map.Entry<String, String> buildTopicNamesMap(String actionName, String entity, TopicOperation operation) {
        return Map.entry(
                TopicUtils.buildTopicPropertyPath(entity, actionName, operation.getName()),
                actionName + "-" + entity + "-" + operation.getName());
    }

}
