package com.tw.relife.domain;

public class ValueHolder <T> {
    T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
