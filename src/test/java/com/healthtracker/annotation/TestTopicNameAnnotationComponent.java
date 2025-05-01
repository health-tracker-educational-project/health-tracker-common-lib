package com.healthtracker.annotation;

import com.healthtracker.constant.TopicAction;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TestTopicNameAnnotationComponent {

    @TopicName(entity = "test", action = TopicAction.LIST)
    private String topicName;

}
