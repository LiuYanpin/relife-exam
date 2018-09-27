package com.tw.relife;

import com.tw.relife.testClass.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void should_bind_request_on_controller_with_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneActionController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest getRequest = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse getResponse = app.process(getRequest);
        assertEquals(200, getResponse.getStatus());
        assertEquals("Hello from /path", getResponse.getContent());
    }

    @Test
    void should_throw_exception_if_controller_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(null));
    }

    @Test
    void should_throw_exception_if_controller_is_abstract_or_interface() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(AbstractClass.class));
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(InterfaceClass.class));
    }

    @Test
    void should_throw_exception_if_controller_without_proper_annotation() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(ControllerWithoutAnnotation.class));
    }

    @Test
    void should_throw_exception_if_controller_action_has_more_than_one_parameter_or_wrong_type() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(ActionHasManyParametersController.class));
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder().addController(ActionParameterWrongTypeController.class));
    }

    @Test
    void should_distinguish_different_actions() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(TwoDifferentActionController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest getRequest = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeRequest postRequest = new RelifeRequest("/path", RelifeMethod.POST);

        RelifeResponse getResponse = app.process(getRequest);
        RelifeResponse postResponse = app.process(postRequest);

        assertEquals(403, getResponse.getStatus());
        assertEquals("get action", getResponse.getContent());

        assertEquals(200, postResponse.getStatus());
        assertEquals("post action", postResponse.getContent());
    }

    @Test
    void should_get_same_action_if_controller_has_multi_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(TwoDifferentActionController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest getRequest1 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeRequest getRequest2 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeRequest getRequest3 = new RelifeRequest("/path", RelifeMethod.GET);

        RelifeResponse getResponse1 = app.process(getRequest1);
        RelifeResponse getResponse2 = app.process(getRequest2);
        RelifeResponse getResponse3 = app.process(getRequest3);

        assertEquals(getResponse1.getStatus(), getResponse2.getStatus());
        assertEquals(getResponse1.getContent(), getResponse2.getContent());

        assertEquals(getResponse2.getStatus(), getResponse3.getStatus());
        assertEquals(getResponse2.getContent(), getResponse3.getContent());
    }

    @Test
    void should_get_status_code_if_action_throw_exception() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneActionWithExceptionController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest getRequest = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse getResponse = app.process(getRequest);

        assertEquals(500, getResponse.getStatus());
    }

    @Test
    void should_add_many_controller_and_differ_different_actions_in_controller() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(FirstControllerWithOneAction.class)
                .addController(SecondControllerWithOneAction.class)
                .build();
        RelifeApp app = new RelifeApp(handler);

        RelifeRequest getRequest = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse getResponse = app.process(getRequest);
        RelifeRequest postRequest = new RelifeRequest("/path", RelifeMethod.POST);
        RelifeResponse postResponse = app.process(postRequest);

        assertEquals(200, getResponse.getStatus());
        assertEquals("method form first controller", getResponse.getContent());
        assertEquals(403, postResponse.getStatus());
        assertEquals("method form second controller", postResponse.getContent());

    }

    @Test
    void should_get_first_action_add_many_controller_if_controllers_have_same_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(FirstControllerWithSameAction.class)
                .addController(SecondControllerWithSameAction.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest getRequest1 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse getResponse1 = app.process(getRequest1);
        assertEquals(200, getResponse1.getStatus());
        assertEquals("action form first controller", getResponse1.getContent());

        RelifeRequest getRequest2 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse getResponse2 = app.process(getRequest2);
        assertEquals(200, getResponse2.getStatus());
        assertEquals("action form first controller", getResponse2.getContent());
    }

    @Test
    void should_throw_exception_if_add_same_controller() {
        assertThrows(IllegalArgumentException.class, () -> new RelifeMvcHandlerBuilder()
                .addController(FirstControllerWithSameAction.class)
                .addController(FirstControllerWithSameAction.class));
    }
}
