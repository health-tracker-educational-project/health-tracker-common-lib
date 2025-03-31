package com.healthtracker.kafka;


import com.healthtracker.wrapper.RecordWrapper;

public abstract class GeneralKafkaConfig {

    protected static final String TYPE_MAPPING = "wrapper:%s".formatted(RecordWrapper.class.getName());
    protected static final Class<?> DEFAULT_TYPE = RecordWrapper.class;

}
