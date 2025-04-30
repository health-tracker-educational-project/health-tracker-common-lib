package com.healthtracker.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TestTopicNameAnnotationComponent.class, TopicValueAnnotationBeanPostProcessor.class})
@ActiveProfiles("test")
public class TopicNameAnnotationTest {

    @MockitoSpyBean
    private TestTopicNameAnnotationComponent component;

    @Test
    public void testAnnotation_withValidData_successfullyComplete() {
        String expectedTopic = "list-test-request";
        assertEquals(expectedTopic, component.getTopicName());
    }

}
