package org.example;

import java.util.ArrayList;

public interface DatabaseInterface {
    public static final String MESSAGEFILENAMES = "MessageFileNames.txt"; //String name of our message file
    public static final String USERFILENAMES = "UserFileNames.txt"; //String name of our user file

    public boolean containsIndexingCharacters(String toCheck); //Check if string contains separation characters.

    public boolean createOldUsers(); //Reads the messages in the file "MessageFileNames.txt" and adds them to the message ArrayList.

    public String createNewUser(String username, String password); //Creates a new user object, checks to see if valid, if valid: creates and adds to arraylist. Otherwise returns error message.

    public boolean createOldMessages(); //Reads the user information in "UserFileNames.txt" and adds them to the user ArrayList.

    public int getUserIndex(String username); //Gets the index of a user with username 'username' in the arraylist of users

    public int getUserIndex(User user); //Gets the index of a user in the arraylist of users

    public boolean createUserFile(); //Creates the username file for when we need persistent data

    public boolean createMessageFile(); //Creates the message file for when we need persistent data

    public ArrayList<User> getUsers(); //Returns the array of users

    public ArrayList<Message> getMessages(); //Returns the array of messages

    public boolean addMessage(User user1, User user2, String message); //Checks to see if the message contains separation characters, finds message object then calls add message. If object doesn't exist, creates a new message object.

    public void deleteMessage(String user1, String user2, int index); //Checks to see if message object exists. If it exists: call remove message. Returns false if message isn't removed. Returns true if it is.

    public boolean userExists(String username);    // userExists - checks if a string of a username is in the USERLIST of the database and returns whether it is.

    /* Nth index of, imported from apache commons */
    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }


}

