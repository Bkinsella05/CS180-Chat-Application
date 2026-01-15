package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTestClass {

    private User user1;
    private User user2;
    private Message message;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("user1", "password1");
        this.user2 = new User("user2", "password2");
        this.message = new Message(user1, user2, "user1:Hello`user2:Hi`");
    }

    @Test
    public void testConstructorWithMessage() {
        assertNotNull(message);
        assertEquals(2, message.getMessages().size());
        assertEquals("user1:Hello", message.getMessages().get(0));
        assertEquals("user2:Hi", message.getMessages().get(1));
    }

    @Test
    public void testConstructorBlockedUser() {
        user1.getBlockedUsers().add("user2");
        Exception exception = assertThrows(BlockedUserException.class, () -> {
            new Message(user1, user2);
        });
        assertEquals("User Blocked: Can't send message", exception.getMessage());
    }

    @Test
    public void testCreateNewMessage() {
        message.createNewMessage("user1:Hey Dude!");
        System.out.println(message.toString());
        assertEquals(3, message.getMessages().size()); // Assumes user input adds a new message

    }

    @Test
    public void testCreateNewMessageBlocked() {
        user1.getBlockedUsers().add("user2");
        message.createNewMessage("user2:Hey ");
        assertEquals(2, message.getMessages().size()); // No new message should be added
    }

    @Test
    public void testRemoveMessage() {
        message.removeMessage(0);
        assertEquals(1, message.getMessages().size());
        assertFalse(message.getMessages().contains("user1:Hello"));
    }

    @Test
    public void testRemoveMessageNotPresent() {
        int initialSize = message.getMessages().size();
        assertFalse(message.removeMessage(5));
        assertEquals(initialSize, message.getMessages().size()); // Size should remain the same
    }

    @Test
    public void testReadMessageValidIndex() {
        assertEquals("user1:Hello", message.readMessage(0));
    }

    @Test
    public void testReadMessageInvalidIndex() {
        assertEquals("", message.readMessage(10)); // Out of bounds
    }

    @Test
    public void testGetMessages() {
        ArrayList<String> messages = message.getMessages();
        assertNotNull(messages);
        assertEquals(2, messages.size());
    }

    @Test
    public void testToString() {
        assertEquals("user1|user2|user1:Hello`user2:Hi`", message.toString());
    }
}
