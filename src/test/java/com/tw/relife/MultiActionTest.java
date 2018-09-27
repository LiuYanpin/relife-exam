package com.tw.relife;

import org.junit.jupiter.api.Test;

public class MultiActionTest {
    @Test
    void should_map_multi_diffrent_actions() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",RelifeMethod.GET,
                        request -> new RelifeResponse(200, "get action", "text/plain"))
                .addAction(
                        "/path",RelifeMethod.POST,
                        request -> new RelifeResponse(403, "post action", "text/plain"))
                .build();

    }
}
