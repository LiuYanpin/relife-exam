package com.tw.relife.testClass;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;

@RelifeController
public class SecondControllerWithOneAction {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.POST)
    public RelifeResponse secondMethod(RelifeRequest request) {
        return new RelifeResponse(
                403,
                "method form second controller",
                "text/plain"
        );
    }
}

