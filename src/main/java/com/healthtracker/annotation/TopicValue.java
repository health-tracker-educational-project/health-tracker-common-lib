package com.healthtracker.annotation;

import com.healthtracker.constant.TopicAction;
import com.healthtracker.constant.TopicHttpType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TopicValue {

    String entity() default "";
    TopicAction action() default TopicAction.GET;
    TopicHttpType httpType() default TopicHttpType.REQUEST;

}
