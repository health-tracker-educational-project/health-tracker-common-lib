package com.healthtracker.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "kafka")
@Configuration
@Getter
@Setter
public class TopicEntities {

    private List<String> entities;

}
