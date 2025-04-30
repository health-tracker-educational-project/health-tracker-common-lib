package com.healthtracker.annotation;

import com.healthtracker.constant.TopicAction;
import com.healthtracker.constant.TopicOperation;
import com.healthtracker.util.TopicUtils;
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

    private final Environment env;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            TopicName annotation = field.getAnnotation(TopicName.class);
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

    private String generateTopicValue(TopicName annotation) {
        String entity = annotation.entity();
        TopicAction action = annotation.action();
        TopicOperation httpType = annotation.operation();

        String propertyPath = TopicUtils.buildTopicPropertyPath(entity, action.name(), httpType.getName());
        String property = env.getProperty(propertyPath);

        if (StringUtils.isBlank(property)) {
            throw new InvalidConfigurationPropertyNameException("Env property '" + propertyPath + "' is not exist!", List.of());
        }

        return property;
    }

}
