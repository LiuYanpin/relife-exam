package com.tw.relife.testClass;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;

@RelifeController
public class SameActionController {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse firstMethod(RelifeRequest request) {
        return new RelifeResponse(
                200,
                "first action",
                "text/plain"
        );
    }

    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse secondMethod(RelifeRequest request) {
        return new RelifeResponse(
                403,
                "second action",
                "text/plain"
        );
    }
}
