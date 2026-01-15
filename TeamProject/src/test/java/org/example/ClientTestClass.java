package org.example;

import java.awt.Container;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientTestClass {
    @Test
    public void testRegisterOrLoginMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("registerOrLogin", Container.class);
        assertNotNull(method, "Method registerOrLogin should exist");
    }

    @Test
    public void testLoginButtonClickedMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("loginButtonClicked", Container.class);
        assertNotNull(method, "Method loginButtonClicked should exist");
    }

    @Test
    public void testRegisterButtonClickedMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("registerButtonClicked", Container.class);
        assertNotNull(method, "Method registerButtonClicked should exist");
    }


    @Test
    public void testRunMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("run");
        assertNotNull(method, "Method run should exist");
    }

    @Test
    public void testMainMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("main", String[].class);
        assertNotNull(method, "Method main should exist");
    }

    @Test
    public void testRunAfterLoginMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("runAfterLogin", Container.class, String.class, String.class);
        assertNotNull(method, "Method runAfterLogin should exist");
    }

    @Test
    public void testDisplayResultsMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("displayResults", JPanel.class, ArrayList.class);
        assertNotNull(method, "Method displayResults should exist");
    }

    @Test
    public void testMessageBoxMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("messageBox", String.class, String[].class, boolean.class);
        assertNotNull(method, "Method MessageBox should exist");
    }
    //bigWhiteBoxOtherUserView
    @Test
    public void testBigWhiteBoxOtherUserViewMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("bigWhiteBoxOtherUserView", String.class, String.class, boolean.class, boolean.class);
        assertNotNull(method, "Method bigWhiteBoxOtherUserView should exist");
    }

    @Test
    public void testUpdateButtonsMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("updateButtons", String.class, boolean.class, boolean.class);
        assertNotNull(method, "Method updateButtons should exist");
    }
    //bigWhiteBoxMyView String String
    @Test
    public void testBigWhiteBoxMyViewMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("bigWhiteBoxMyView", String.class, String.class);
        assertNotNull(method, "Method bigWhiteBoxMyView should exist");
    }

    @Test
    public void testGetMessageUsernamesMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getMessageUsernames");
        assertNotNull(method, "Method getMessageUsernames should exist");
    }

    @Test
    public void testGetFriendUsernamesMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getFriendUsernames");
        assertNotNull(method, "Method getFriendUsernames should exist");
    }

    @Test
    public void testGetBlockedUsernamesMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getBlockedUsernames");
        assertNotNull(method, "Method getBlockedUsernames should exist");
    }

    @Test
    public void testGetMyUserMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getMyUser");
        assertNotNull(method, "Method getMyUser should exist");
    }

    @Test
    public void testSetMyUserInfoMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("setMyUserInfo", String.class);
        assertNotNull(method, "Method setMyUserInfo should exist");
    }

    @Test
    public void testGetUserMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getUser", String.class);
        assertNotNull(method, "Method getUser should exist");
    }

    @Test
    public void testGetMessageHistoryMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("getMessageHistory", String.class);
        assertNotNull(method, "Method getMessageHistory should exist");
    }

    @Test
    public void testSendMessageMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("sendMessage", String.class, String.class);
        assertNotNull(method, "Method sendMessage should exist");
    }

    @Test
    public void testAddFriendMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("addFriend", String.class);
        assertNotNull(method, "Method addFriend should exist");
    }

    @Test
    public void testRemoveFriendMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("removeFriend", String.class);
        assertNotNull(method, "Method removeFriend should exist");
    }

    @Test
    public void testBlockUserMethodExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("blockUser", String.class);
        assertNotNull(method, "Method blockUser should exist");
    }

    @Test
    public void testUnblockUserExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("unblockUser", String.class);
        assertNotNull(method, "Method unblockUser should exist");
    }

    @Test
    public void testSearchUserExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("searchUser", String.class);
        assertNotNull(method, "Method searchUser should exist");
    }

    @Test
    public void testDeleteMessageExists() throws NoSuchMethodException {
        Method method = Client.class.getMethod("deleteMessage", String.class, int.class);
        assertNotNull(method, "Method deleteMessage should exist");
    }





    //getMessageUsernames
    //getFriendUsernames
}
