package com.tw.relife.domain;

import com.tw.relife.RelifeAppHandler;
import com.tw.relife.RelifeMethod;

import java.lang.reflect.Method;

public class Action {
    private String path;
    private RelifeMethod method;
    private RelifeAppHandler handler;


    private Method methodName;
    public Action() {
    }

    public Action(String path, RelifeMethod method, RelifeAppHandler handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public Action(String path, RelifeMethod method, Method methodName) {
        this.path = path;
        this.method = method;
        this.methodName = methodName;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RelifeMethod getMethod() {
        return method;
    }

    public void setMethod(RelifeMethod method) {
        this.method = method;
    }

    public RelifeAppHandler getHandler() {
        return handler;
    }

    public void setHandler(RelifeAppHandler handler) {
        this.handler = handler;
    }

    public Method getMethodName() {
        return methodName;
    }

    public void setMethodName(Method methodName) {
        this.methodName = methodName;
    }
}
