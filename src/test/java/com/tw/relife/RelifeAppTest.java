package com.tw.relife;

import com.tw.relife.exception.RelifeStatusCode;
import com.tw.relife.exception.SampleNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelifeAppTest {
    @Test
    void should_create_response() {
        RelifeApp app = new RelifeApp(request -> new RelifeResponse(200));

        RelifeRequest whateverRequest = new RelifeRequest("/any/path", RelifeMethod.GET);
        RelifeResponse response = app.process(whateverRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void should_throw_for_null_handler() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new RelifeApp(null)
        );
    }

    @Test
    void should_catch_unhandled_exception_as_internal_server_error() {
        RelifeApp app = new RelifeApp(request -> {
            throw new RuntimeException("error occurred");
        });

        RelifeRequest whateverRequest = new RelifeRequest("/any/path", RelifeMethod.GET);
        RelifeResponse response = app.process(whateverRequest);

        assertEquals(500, response.getStatus());
    }

    @Test
    void should_get_sample_not_found_exception() {
        RelifeApp app = new RelifeApp(request -> {
            throw new SampleNotFoundException();
        });
        RelifeRequest whateverRequest = new RelifeRequest("/any/path", RelifeMethod.GET);
        RelifeResponse response = app.process(whateverRequest);

        assertEquals(404, response.getStatus());
    }

    @Test
    void should_bind_request_to_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> new RelifeResponse(200, "Hello", "text/plain"))
                .build();

        RelifeApp app = new RelifeApp(handler);

        RelifeResponse response = app.process(
                new RelifeRequest("/path", RelifeMethod.GET));

        assertEquals(200, response.getStatus());
        assertEquals("Hello", response.getContent());
    }
    @Test
    void should_get_exception_if_path_or_method_is_unmatched() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> new RelifeResponse(200, "World", "text/plain"))
                .build();

        RelifeApp app = new RelifeApp(handler);

        RelifeResponse response1 = app.process(
                new RelifeRequest("/path/string", RelifeMethod.GET));
        assertEquals(404, response1.getStatus());

        RelifeResponse response2 = app.process(
                new RelifeRequest("/path", RelifeMethod.POST));
        assertEquals(404, response2.getStatus());
    }

    @Test
    void should_throw_exception_if_one_of_add_action_parameters_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder()
                .addAction(null, RelifeMethod.GET, request -> new RelifeResponse(200)));

        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder()
                .addAction("/path", null, request -> new RelifeResponse(200)));

        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder()
                .addAction("/path", RelifeMethod.GET, null));
    }

    @Test
    void should_get_response_of_200_if_handler_return_null() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction("/path", RelifeMethod.GET, request -> null)
                .build();

        RelifeApp app = new RelifeApp(handler);

        RelifeResponse response = app.process(
                new RelifeRequest("/path", RelifeMethod.GET));
        assertEquals(200, response.getStatus());
        assertNull(response.getContent());

    }

    @Test
    void should_be_compatible_of_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RelifeApp(null);
        });
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction("/path", RelifeMethod.GET, request -> {
                    throw new RuntimeException("error occurred");
                }).build();
        RelifeApp app = new RelifeApp(handler);
        RelifeResponse response = app.process(
                new RelifeRequest("/path", RelifeMethod.GET));
        assertEquals(500, response.getStatus());
    }

    @Test
    void should_be_compatible_of_exception_from_annotation() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction("/path", RelifeMethod.GET, request -> {
                    throw new SampleNotFoundException();
                }).build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest request = new RelifeRequest(
                "/path", RelifeMethod.GET
        );
        RelifeResponse response = app.process(request);
        assertEquals(404, response.getStatus());
    }
}






















