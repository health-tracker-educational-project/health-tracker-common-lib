package com.healthtracker.annotation;

import com.healthtracker.constant.TopicAction;
import com.healthtracker.constant.TopicHttpType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyNameException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final String TOPICS_PREFIX = "kafka.topics";
    private final Environment env;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            TopicValue annotation = field.getAnnotation(TopicValue.class);
            if (annotation != null) {
                String value = generateTopicValue(annotation);
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, value);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private String generateTopicValue(TopicValue annotation) {
        String entity = annotation.entity();
        TopicAction action = annotation.action();
        TopicHttpType httpType = annotation.httpType();

        String propertyPath = buildPropertyPath(entity, action, httpType);
        String property = env.getProperty(propertyPath);

        if (StringUtils.isBlank(property)) {
            throw new InvalidConfigurationPropertyNameException("Env property '" + propertyPath + "' is not exist!", List.of());
        }

        return property;
    }

    private String buildPropertyPath(String entity, TopicAction action, TopicHttpType httpType) {
        return TOPICS_PREFIX + "." + entity.toLowerCase() + "." + action.name().toLowerCase() + "-" + httpType.name().toLowerCase();
    }

}
