package com.healthtracker.annotation;

import com.healthtracker.constant.TopicAction;
import com.healthtracker.constant.TopicOperation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TopicName {

    String entity();
    TopicAction action() default TopicAction.GET;
    TopicOperation operation() default TopicOperation.REQUEST;

}
