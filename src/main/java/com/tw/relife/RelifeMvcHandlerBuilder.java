package com.tw.relife;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;
import com.tw.relife.domain.Action;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RelifeMvcHandlerBuilder implements RelifeAppHandler{
    private List<Action> actions = new ArrayList<>();
    private Set<Class> controllers = new HashSet<>();
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
        judgeIllegalController(controllerClass);
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

    private void judgeIllegalController(Class controllerClass) {
        if (controllerClass == null
                || controllerClass.isInterface()
                || Modifier.isAbstract(controllerClass.getModifiers())
                || controllerClass.getAnnotation(RelifeController.class) == null
        ) {
            throw new IllegalArgumentException();
        }

        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method: methods) {
            if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].equals(RelifeRequest.class)) {
                throw new IllegalArgumentException();
            }
        }

        if (controllers.contains(controllerClass)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public RelifeResponse process(RelifeRequest request) {
        RelifeResponse response = actions.stream()
                .filter(item -> item.getPath().equals(request.getPath()) && item.getMethod().equals(request.getMethod()))
                .findFirst()
                .map(action -> getRelifeResponse(request, action))
                .orElse(new RelifeResponse(404));

        return response;

    }

    private RelifeResponse getRelifeResponse(RelifeRequest request, Action action) {
        RelifeResponse relifeResponse;
        if (action.getHandler() != null) {
            relifeResponse = action.getHandler().process(request);
            if (relifeResponse == null) {
                relifeResponse =  new RelifeResponse(200);
            }
        }else {
            try {
                Method method = action.getMethodName();
                method.setAccessible(true);
                Object invokeResult = method.invoke(method.getDeclaringClass().newInstance(), request);
                if (invokeResult == null) {
                    relifeResponse = new RelifeResponse(200);
                }else if (invokeResult.getClass().equals(RelifeResponse.class)) {
                    relifeResponse = (RelifeResponse) invokeResult;
                }else {
                    relifeResponse = new RelifeResponse(200, new ObjectMapper().writeValueAsString(invokeResult), "application/json");
                }
                method.setAccessible(false);
            }catch (Exception e) {
                relifeResponse = new RelifeResponse(500);
            }
        }
        return relifeResponse;
    }

}
