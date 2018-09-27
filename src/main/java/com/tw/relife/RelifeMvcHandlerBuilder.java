package com.tw.relife;

import com.tw.relife.domain.Action;
import com.tw.relife.domain.ValueHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelifeMvcHandlerBuilder implements RelifeAppHandler{
    private List<Action> actions = new ArrayList<>();
    private boolean buildFlag = false;

    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler handler) {
        if (buildFlag) {
            throw new IllegalArgumentException("can't add action");
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
    @Override
    public RelifeResponse process(RelifeRequest request) {

        RelifeResponse response = actions.stream()
                .filter(item -> item.getPath().equals(request.getPath()) && item.getMethod().equals(request.getMethod()))
                .findFirst()
                .map(action -> {
                    RelifeResponse relifeResponse = action.getHandler().process(request);
                    if (relifeResponse == null) {
                        relifeResponse =  new RelifeResponse(200);
                    }
                    return relifeResponse;
                })
                .orElse(new RelifeResponse(404));

        return response;

    }
}
