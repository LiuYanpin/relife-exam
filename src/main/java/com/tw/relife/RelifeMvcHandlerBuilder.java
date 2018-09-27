package com.tw.relife;

import com.tw.relife.annotation.RelifeRequestMapping;
import com.tw.relife.domain.Action;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RelifeMvcHandlerBuilder implements RelifeAppHandler{
    private List<Action> actions = new ArrayList<>();
    private List<Class> controllers = new ArrayList<>();

    private boolean buildFlag = false;

    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler handler) {
        if (buildFlag) {
            throw new IllegalArgumentException("You can't add action anymore.");
        }
        if (path == null || method == null || handler == null) {
            throw new IllegalArgumentException();
        }
        actions.add(new Action(path, method, handler));
        return this;
    }

    public RelifeAppHandler build() {
        buildFlag = true;
        return this;
    }

    public RelifeMvcHandlerBuilder addController(Class controllerClass) {
        if (controllerClass == null) {
            throw new IllegalArgumentException();
        }
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method: methods) {
            method.setAccessible(true);
            RelifeRequestMapping annotation = method.getAnnotation(RelifeRequestMapping.class);
            if (annotation != null) {
                actions.add(new Action(annotation.value(), annotation.method(), method));
            }
            method.setAccessible(false);
        }
        controllers.add(controllerClass);
        return this;
    }

    @Override
    public RelifeResponse process(RelifeRequest request) {
        RelifeResponse response = actions.stream()
                .filter(item -> item.getPath().equals(request.getPath()) && item.getMethod().equals(request.getMethod()))
                .findFirst()
                .map(action -> {
                    RelifeResponse relifeResponse;
                    if (action.getHandler() != null) {
                        relifeResponse = action.getHandler().process(request);
                        if (relifeResponse == null) {
                            relifeResponse =  new RelifeResponse(200);
                        }
                    }else {
                        try {
                            relifeResponse = (RelifeResponse) action.getMethodName().invoke(controllers.get(0).newInstance(), request);
                            if (relifeResponse == null) {
                                relifeResponse =  new RelifeResponse(200);
                            }
                        }catch (Exception e) {
                            relifeResponse = new RelifeResponse(500);
                        }
                    }
                    return relifeResponse;
                })
                .orElse(new RelifeResponse(404));

        return response;

    }

}
