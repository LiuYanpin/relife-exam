package com.tw.relife.testClass;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;

@RelifeController
public class ActionParameterWrongTypeController {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse sayHello(RelifeResponse request) {
        return new RelifeResponse(200);
    }
}
