package com.tw.relife.testClass;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.annotation.RelifeController;
import com.tw.relife.annotation.RelifeRequestMapping;

@RelifeController
public class ControllerWithPojoReturnAction {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.POST)
    public PojoReturnValue getPoJo(RelifeRequest request) {
        return new PojoReturnValue("liuyanping", 18);
    }

    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public Object getNull(RelifeRequest request) {
        return null;
    }
}
