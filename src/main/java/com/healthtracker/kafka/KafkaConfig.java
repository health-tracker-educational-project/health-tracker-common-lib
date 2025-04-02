package com.healthtracker.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    private final static Integer PARTITIONS = 2;
    private final static Short REPLICATION_FACTOR = 1;

    @Value("${spring.kafka.admin.properties.bootstrap.servers}")
    private String kafkaAdminBootstrapServers;


    @Bean
    public Topics topics(TopicEntities topicEntities, ConfigurableEnvironment environment) {
        return new Topics(topicEntities, environment);
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAdminBootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics initializeTopics(Topics topics) {
        return new KafkaAdmin.NewTopics(
                topics.getTopics().stream()
                        .map(topic -> new NewTopic(topic, PARTITIONS, REPLICATION_FACTOR))
                        .toArray(NewTopic[]::new)
        );
    }

    @Bean
    public RecordMessageConverter messageConverter() {
        return new JsonMessageConverter();
    }

}
