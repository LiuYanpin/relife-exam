package com.tw.relife;

public class RelifeMvcHandlerBuilder implements RelifeAppHandler{
    private RelifeResponse response;
    private RelifeAppHandler handler;
    private RelifeRequest request;



    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler handler) {
        this.request = new RelifeRequest(path, method);
        this.handler = handler;
        return this;
    }
    public RelifeAppHandler build() {
        return this;
    }
    @Override
    public RelifeResponse process(RelifeRequest request) {
        return this.response = handler.process(request);
    }
}
