package com.healthtracker.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TestTopicNameAnnotationComponent.class, TopicValueAnnotationBeanPostProcessor.class})
@ActiveProfiles("test")
public class TopicNameAnnotationTest {

    @Autowired
    private TestTopicNameAnnotationComponent component;

    @Test
    public void testAnnotation_withValidData_successfullyComplete() {
        String expectedTopic = "list-test-request";
        assertEquals(expectedTopic, component.getTopicName());
    }

}
