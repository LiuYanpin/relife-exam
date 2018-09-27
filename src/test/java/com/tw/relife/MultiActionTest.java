package com.tw.relife;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiActionTest {
    @Test
    void should_map_multi_different_actions() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path", RelifeMethod.GET,
                        request -> new RelifeResponse(200, "get action", "text/plain"))
                .addAction(
                        "/path", RelifeMethod.POST,
                        request -> new RelifeResponse(403, "post action", "text/plain"))
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest postRequest = new RelifeRequest("/path", RelifeMethod.POST);
        RelifeRequest getRequest = new RelifeRequest("/path", RelifeMethod.GET);

        RelifeResponse postResponse = app.process(postRequest);
        RelifeResponse getResponse = app.process(getRequest);

        assertEquals(403, postResponse.getStatus());
        assertEquals("post action", postResponse.getContent());

        assertEquals(200, getResponse.getStatus());
        assertEquals("get action", getResponse.getContent());
    }
}
