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

    @Test
    void should_use_first_mapping_rule_if_have_same_match_rule() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path", RelifeMethod.POST,
                        request -> new RelifeResponse(200, "first action", "text/plain"))
                .addAction(
                        "/path", RelifeMethod.POST,
                        request -> new RelifeResponse(403, "second action", "text/plain"))
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest postRequest = new RelifeRequest("/path", RelifeMethod.POST);
        RelifeResponse postResponse = app.process(postRequest);
        assertEquals(200, postResponse.getStatus());
        assertEquals("first action", postResponse.getContent());

    }


}
