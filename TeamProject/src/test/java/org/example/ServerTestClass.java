package org.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTestClass {
    @Test
    public void testRunMethodExists() throws NoSuchMethodException {
        Method method = Server.class.getMethod("run");
        assertNotNull(method, "Method run should exist");
    }

    @Test
    public void testUpdateOtherUserMethodExists() throws NoSuchMethodException {
        Method method = Server.class.getMethod("updateOtherUser", String.class, Object[].class);
        assertNotNull(method, "Method updateOtherUser should exist");
    }

    @Test
    public void testMainMethodExists() throws NoSuchMethodException {
        Method method = Server.class.getMethod("main", String[].class);
        assertNotNull(method, "Method main should exist");
    }
}
