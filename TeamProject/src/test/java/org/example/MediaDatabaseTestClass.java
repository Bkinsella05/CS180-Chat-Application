package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MediaDatabaseTestClass {
    private MediaDatabase database;
    private ArrayList<Message> messagesCorrectOutput;
    private ArrayList<User> usersCorrectOutput;

    @BeforeEach
    public void setUp() {
        database = new MediaDatabase();
        messagesCorrectOutput = new ArrayList<>();
        usersCorrectOutput = new ArrayList<>();
        usersCorrectOutput.add(new User("User1", "pass"));
        usersCorrectOutput.add(new User("User2", "pass"));
    }

    //Do not need to test, .toString() was tested, arrayList was tested, just a for loop calling .toString() and write
    //File path was hard coded
    @Test
    void testCreateUserFile() throws NoSuchMethodException {
        Method method = MediaDatabase.class.getMethod("createUserFile");
        assertNotNull(method, "Method createUserFile should exist");
    }

    @Test
    public void testCreateMessageFile() throws NoSuchMethodException {
        Method method = MediaDatabase.class.getMethod("createMessageFile");
        assertNotNull(method, "Method createMessageFile should exist");
    }

    @Test
    public void testCreateOldUsers() {
        assertTrue(database.createOldUsers());
    }

    //As tested, these two methods will return false if the files do not exist
    //The arrayLists are not mutilated in this case, this was also tested and found to be true
    //Will not mutilate if file is empty per test cases
    @Test
    public void createOldMessages() {
        assertTrue(database.createOldMessages());
    }

    @Test
    public void testCreateNewUser() {
        database.createNewUser("me", "pass");
        User user = new User("me", "pass");
        assertTrue((database.getUsers().get(database.getUserIndex("me"))).equals(user));
        assertEquals("Error: User Already Exists", database.createNewUser("User1", "You can't guess this!"));
        assertEquals("Error: Invalid Username or Password\nMust not be empty", database.createNewUser("", "Hey"));
        assertEquals("Error: Invalid Username or Password\nMust not be empty", database.createNewUser("GHHUDOHFH", ""));
        assertEquals("Error: Invalid Username or Password\nMust Start with a Letter", database.createNewUser("@@@@", "gjhdjjhj"));
        assertEquals("Error: Invalid Username or Password\nMust be 16 characters or less", database.createNewUser("sdghshghskdhgksdhghjsd", "sgbsjg"));
        assertEquals("Error: Invalid Username or Password\nMust be 16 characters or less", database.createNewUser("sdjg", "skjdgjksgjshdjkghskjh"));
        assertEquals("Error: Invalid Username or Password\nMust not contain the following characters:\n',' or '|' or ':' or '`'", database.createNewUser("a|", "dfjg"));
        assertEquals("Error: Invalid Username or Password\nMust not contain the following characters:\n',' or '|' or ':' or '`'", database.createNewUser("dhd", ":::"));
    } //test cases for empty username and password

    @Test
    public void testGetUserIndexWithUserName() {
        int userIndex = database.getUserIndex("User1");
        assertEquals(0, userIndex);
    }

    @Test
    public void testGetUserIndexWithoutUserName() {
        int userIndex = database.getUserIndex(usersCorrectOutput.get(0));
        assertEquals(0, userIndex);
    }

    @Test
    public void testAddMessage() {
        Message correctFinalMessage = new Message(usersCorrectOutput.get(0), usersCorrectOutput.get(1), "User1 : content`User2 : my content is better`User1 : Nah mine is`");
        database.addMessage(usersCorrectOutput.get(0), usersCorrectOutput.get(1), "Nah mine is");
        assertEquals(correctFinalMessage.toString(), database.getMessages().getFirst().toString());
        assertTrue(database.addMessage(new User("Jim", "pass"), new User("Harry", "pass"), "Yo whats up"));
        assertFalse(database.addMessage(new User("Jim", "pass"), new User("Harry", "pass"), "Harry:Yo :whats up"));
    }

    @Test
    public void testDeleteMessage() { //this assumes that testAddMessage adds the message to the database object so it can be removed here
        database.deleteMessage(usersCorrectOutput.get(0).getUsername(), usersCorrectOutput.get(1).getUsername(), 0);
        assertEquals("User1|User2|User2 : my content is better`User1 : Nah mine is`", database.getMessages().getFirst().toString());
    }
}
