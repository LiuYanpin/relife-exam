package com.tw.relife.testClass;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;

@RelifeController
public class TwoDifferentActionController {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse getHello(RelifeRequest request) {
        return new RelifeResponse(
                403,
                "get action",
                "text/plain"
        );
    }

    @RelifeRequestMapping(value = "/path", method = RelifeMethod.POST)
    public RelifeResponse postHello(RelifeRequest request) {
        return new RelifeResponse(
                200,
                "post action",
                "text/plain"
        );
    }
}
