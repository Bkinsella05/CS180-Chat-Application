package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.example.MediaDatabase;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class UserTestClass { //User interface has nothing beyond the user class
    private User user;
    private User user2;
    private ArrayList<String> list;
    private ArrayList<String> list2;

    @BeforeEach
    public void setUp() {
        this.list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        this.list2 = new ArrayList<String>();
        list2.add("c");
        list2.add("d");
        this.user = new User("me", "pass", "I'm spongebob", list, list2);
        this.user2 = new User("me", "pass");
    }
    @Test
    public void testOne() { //Nulls are tested in createNewUser method as these constructors are never called otherwise
        assertEquals("me,pass,I'm spongebob,:a|:b|,:c|:d|", user.toString());
        assertEquals("me,pass,,,", user2.toString());
    }

    @Test
    public void testTwo() {
        assertEquals("me", user2.getUsername());
        assertEquals("pass", user2.getPassword());
        assertEquals("", user2.getBio());
        assertTrue(user2.getFriends().isEmpty());
        assertTrue(user2.getBlockedUsers().isEmpty());
    }

    @Test
    public void testThree() {
        assertEquals("me", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("I'm spongebob", user.getBio());
        assertTrue(user.getFriends().get(0).equals("a"));
        assertTrue(user.getBlockedUsers().get(0).equals("c"));
        assertTrue(user.getFriends().get(1).equals("b"));
        assertTrue(user.getBlockedUsers().get(1).equals("d"));
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testSetBio() {
        user.setBio("New Bio");
        assertEquals("New Bio", user.getBio());
    }

    @Test
    void testSetFriends() {
        ArrayList<String> newFriends = new ArrayList<>();
        newFriends.add("user4");
        user.setFriends(newFriends);
        assertEquals(newFriends, user.getFriends());
    }

    @Test
    void testSetBlockedUsers() {
        ArrayList<String> newBlockedUsers = new ArrayList<>();
        newBlockedUsers.add("user5");
        user.setBlockedUsers(newBlockedUsers);
        assertEquals(newBlockedUsers, user.getBlockedUsers());
    }

    @Test
    void testAddFriend() { //test uses isBlocked, which works here so this test case covers it
        assertEquals("User Added!", user.addFriend(user2)); // Assumes user2 exists in DatabaseInterface
        assertTrue(user.getFriends().contains("me"));
    }

    @Test
    void testAddFriendAlreadyFriend() {
        user.addFriend(user2);
        assertEquals("This user is already a friend!", user.addFriend(user2));
    }

    @Test
    void testAddFriendBlockedUser() {
        user.blockUser(user2);
        assertEquals("This user is blocked!", user.addFriend(user2));
    }

    @Test
    void testRemoveFriend() {
        user.addFriend(user2);
        assertEquals("Friend Removed", user.removeFriend(user2));
        assertFalse(user.getFriends().contains("user2"));
    }
    @Test
    void testRemoveFriendNotFound() {
        assertEquals("Friend not found", user.removeFriend(user2));
    }
    @Test
    void testIsFriend() {
        user.addFriend(user2);
        assertTrue(user.isFriend(user2));
    }
}
