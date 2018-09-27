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
        RelifeResponse response = this.handler.process(request);
        return response;
    }
}
