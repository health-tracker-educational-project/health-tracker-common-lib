package com.healthtracker.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


@Configuration
@Slf4j
public class KafkaConsumerConfig extends GeneralKafkaConfig {

    private static final Integer CONCURRENCY_LEVEL = 2;
    private static final Long BACKOFF_MAX_ATTEMPTS = 3L;
    private static final Long BACKOFF_INTERVAL = 1000L;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String listenerBootstrapServers;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.client-id}")
    private String clientId;
    @Value("${kafka.dlt-topic-suffix}")
    private String dltTopicSuffix;

    private final BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition>
            destinationResolver = (consumerRecord, exception) -> {
        log.error("Sending message to DLT: {}, Value:{}, Partition: {}, Offset: {}, Timestamp: {}, Error message: {}, Cause Message: {}",
                consumerRecord.topic() + dltTopicSuffix,
                consumerRecord.value(),
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.timestamp(),
                exception.getMessage(),
                exception.getCause().getLocalizedMessage());
        return new TopicPartition(consumerRecord.topic() + dltTopicSuffix, consumerRecord.partition());
    };

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory(
            DefaultErrorHandler errorHandler, KafkaTemplate<String, Object> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        factory.setConcurrency(CONCURRENCY_LEVEL);
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }

    @Bean
    public KafkaConsumer<String, Object> consumer() {
        return new KafkaConsumer<>(consumerConfigs());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, listenerBootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

        props.put(JsonDeserializer.TYPE_MAPPINGS, TYPE_MAPPING);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, DEFAULT_TYPE);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        return props;
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
        return new DefaultErrorHandler(deadLetterPublishingRecoverer,
                new FixedBackOff(BACKOFF_INTERVAL, BACKOFF_MAX_ATTEMPTS));
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, destinationResolver);
    }

}
