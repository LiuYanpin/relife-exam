package com.tw.relife;

import com.tw.relife.domain.Action;
import com.tw.relife.domain.ValueHolder;

import java.util.ArrayList;
import java.util.List;

public class RelifeMvcHandlerBuilder implements RelifeAppHandler{
    private List<Action> actions = new ArrayList<>();

    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler handler) {
        actions.add(new Action(path, method, handler));
        return this;
    }
    public RelifeAppHandler build() {
        return this;
    }
    @Override
    public RelifeResponse process(RelifeRequest request) {
        RelifeResponse response;
        ValueHolder<RelifeResponse> responseValueHolder = new ValueHolder();
        responseValueHolder.setValue(new RelifeResponse(404));

        actions.forEach(item -> {
            if (request.getPath().equals(item.getPath()) && request.getMethod().equals(item.getMethod())) {
                responseValueHolder.setValue(item.getHandler().process(request));
            }
        });
        return responseValueHolder.getValue();

    }
}
