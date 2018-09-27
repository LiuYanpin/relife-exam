package com.tw.relife;


import com.tw.relife.exception.RelifeStatusCode;

import java.lang.annotation.Annotation;

public class RelifeApp implements RelifeAppHandler {
    private final RelifeAppHandler handler;

    public RelifeApp(RelifeAppHandler handler) {
        // TODO: You can start here
        if (handler == null) {
            throw new IllegalArgumentException();
        }
        this.handler = handler;

    }

    @Override
    public RelifeResponse process(RelifeRequest request) {
        // TODO: You can start here
        RelifeResponse response;
        try {
            response = this.handler.process(request);
        }catch (Exception e) {
            response = getRelifeResponse(e);
        }
        return response;
    }

    private RelifeResponse getRelifeResponse(Exception e) {
        RelifeResponse response;
        if (e.getClass().getAnnotation(RelifeStatusCode.class) != null) {
            response = new RelifeResponse(e.getClass().getAnnotation(RelifeStatusCode.class).value());
        }else {
            response = new RelifeResponse(500);
        }
        return response;
    }
}
