package com.tw.relife;

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
        RelifeResponse response = null;
        try {
            response = this.handler.process(request);
        } catch (Exception e) {
            response = new RelifeResponse(500);
        }
        return response;
    }
}
