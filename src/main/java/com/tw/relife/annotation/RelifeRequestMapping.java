package com.tw.relife.annotation;

import com.tw.relife.RelifeMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelifeRequestMapping {
    String value();
    RelifeMethod method();
}
