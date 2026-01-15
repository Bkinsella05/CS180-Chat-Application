package org.example;

import java.io.*;
//import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.*;
/**
 * Media Database
 * The program that stores all of the data for all the users and messages. It writes and reads from files on the close and open of the server and closes on the client.
 *
 * @author Hayden Raffieed, Stephen Shirmeyer, Robert Kinsella, Avneet Kaur Anand
 *
 * @date 12/8/24
 *
 */
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MediaDatabase implements DatabaseInterface {
    private final static ArrayList<User> USER_DATABASE = new ArrayList<User>(); //arraylist of users from file
    private final static ArrayList<Message> MESSAGE_DATABASE = new ArrayList<Message>(); //arraylist of messages from file
    private static final Object SYNC_LOCK = new Object();

    public MediaDatabase() { //Create a new media database object from the user and message file
        createOldUsers();
        createOldMessages();
    }

    public boolean createOldUsers() {
        try {
            //Username,password,bio,friends:[|],blocked:[|]
            //friends:[],
            BufferedReader br = new BufferedReader(new FileReader(new File(USERFILENAMES)));
            String line = "";
            synchronized (SYNC_LOCK) {
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    //Gets all aspects of user from the line containing all their info
                    ArrayList<String> friends = new ArrayList<String>();
                    ArrayList<String> blocked = new ArrayList<String>();
                    String userName = line.substring(0, DatabaseInterface.ordinalIndexOf(line, ",", 1));
                    String password = line.substring(DatabaseInterface.ordinalIndexOf(line, ",", 1) + 1, DatabaseInterface.ordinalIndexOf(line, ",", 2));
                    String bio = line.substring(DatabaseInterface.ordinalIndexOf(line, ",", 2) + 1, DatabaseInterface.ordinalIndexOf(line, ",", 3));
                    String friendList = line.substring(DatabaseInterface.ordinalIndexOf(line, ",", 3) + 1, DatabaseInterface.ordinalIndexOf(line, ",", 4));
                    String blockedList = line.substring(DatabaseInterface.ordinalIndexOf(line, ",", 4) + 1);
                    if (!blockedList.isEmpty()) {
                        for (int index = 1; DatabaseInterface.ordinalIndexOf(blockedList, ":", index) >= 0; index++) {
                            blocked.add(blockedList.substring(DatabaseInterface.ordinalIndexOf(blockedList, ":", index) + 1, DatabaseInterface.ordinalIndexOf(blockedList, "|", index)));
                        }
                    }
                    if (!friendList.isEmpty()) {
                        for (int index = 1; DatabaseInterface.ordinalIndexOf(friendList, ":", index) >= 0; index++) {
                            friends.add(friendList.substring(DatabaseInterface.ordinalIndexOf(friendList, ":", index) + 1, DatabaseInterface.ordinalIndexOf(friendList, "|", index)));
                        }
                    }
                    //Creates new user, all WILL be valid
                    USER_DATABASE.add(new User(userName, password, bio, friends, blocked));
                }
                br.close();
            }

        } catch (Exception ioe) {
            return false;
        }
        return true;
    }

    //boolean doesn't contain select special characters
    public boolean containsIndexingCharacters(String toCheck) {
        if (toCheck.contains(",") || toCheck.contains("|") || toCheck.contains(":") || toCheck.contains("`")) {
            return true;
        }
        return false;
    }

    public String createNewUser(String username, String password) {
        try {
            synchronized (SYNC_LOCK) {
                // Show this to instructor for lab
                if (userExists(username)) {
                    return "Error: User Already Exists";
                }
                if (password.isEmpty() || username.isEmpty()) {
                    return "Error: Invalid Username or Password\nMust not be empty";
                }
                if (!(Character.isLetter(username.charAt(0)) || Character.isLowerCase(username.charAt(0)))) { //username.substring(0, 1).isUpper();
                    return "Error: Invalid Username or Password\nMust Start with a Letter";
                }
                if (username.length() > 16 || password.length() > 16) {
                    return "Error: Invalid Username or Password\nMust be 16 characters or less";
                }
                if (containsIndexingCharacters(username) || containsIndexingCharacters(password)) {
                    return "Error: Invalid Username or Password\nMust not contain the following characters:\n',' or '|' or ':' or '`'";
                }
                User user = new User(username, password);
                USER_DATABASE.add(user);
                return "User Successfully Created";
            }

        } catch (Exception e) {
            return "Invalid Input";
        }
    }

    public boolean createUserFile() { //creates a new file or overwrites an existing one to update the file of user information
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(USERFILENAMES), false))) {
            synchronized (SYNC_LOCK) {
                for (User user : USER_DATABASE) {
                    bw.write(user.toString() + "\n");
                }
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createOldMessages() {
        //Message line : User1|User2|message1, message2, ...
        try {
            BufferedReader br = new BufferedReader(new FileReader(MESSAGEFILENAMES));
            String line = "";
            synchronized (SYNC_LOCK) {
                while ((line = br.readLine()) != null) {
                    //Gets username
                    String username1 = line.substring(0, DatabaseInterface.ordinalIndexOf(line, "|", 1));
                    String username2 = line.substring(DatabaseInterface.ordinalIndexOf(line, "|", 1) + 1, DatabaseInterface.ordinalIndexOf(line, "|", 2));
                    String messages = line.substring(DatabaseInterface.ordinalIndexOf(line, "|", 2) + 1);
                    User user1 = null;
                    User user2 = null;

                    for (User user : USER_DATABASE) {
                        if (user.getUsername().equals(username1)) {
                            user1 = user;
                        }
                        if (user.getUsername().equals(username2)) {
                            user2 = user;
                        }
                    }
                    MESSAGE_DATABASE.add(new Message(user1, user2, messages));
                }
            }
        } catch (Exception ioe) {
            return false;
        }
        return true;
    }

    public boolean createMessageFile() { //Creates a file to store all message information or overwrites the existing one with updated information
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(MESSAGEFILENAMES), false))) {
            synchronized (SYNC_LOCK) {
                for (Message message : MESSAGE_DATABASE) {
                    bw.write(message.toString() + "\n");
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int getUserIndex(String username) { //returns given username's index, or -1 if it doesn't exist
        synchronized (SYNC_LOCK) {
            for (int i = 0; i < USER_DATABASE.size(); i++) {
                if (USER_DATABASE.get(i).getUsername().equals(username)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public synchronized int getUserIndex(User user) { //returns given user objects index, or -1 if it doesn't exist
        synchronized (SYNC_LOCK) {
            for (int i = 0; i < USER_DATABASE.size(); i++) {
                if (USER_DATABASE.get(i).getUsername().equals(user.getUsername())) {
                    return i;
                }
            }
            return -1;
        }
    }

    public ArrayList<User> getUsers() {
        synchronized (SYNC_LOCK) {return USER_DATABASE;}
    }

    public ArrayList<Message> getMessages() {
        synchronized (SYNC_LOCK) {return MESSAGE_DATABASE;}
    }

    public boolean addMessage(User user1, User user2, String message) {
        synchronized (SYNC_LOCK) {
            try {//String message has to be formatted user:content
                message = user1.getUsername() + " : " + message;
                Message m = new Message(user1, user2, message);
                String messageContent = message.substring(message.indexOf(":") + 1);
                if (containsIndexingCharacters(messageContent)) {
                    return false;
                }
                for (int i = 0; i < MESSAGE_DATABASE.size(); i++) {
                    if (MESSAGE_DATABASE.get(i).equals(m)) {
                        Message n = MESSAGE_DATABASE.get(i);
                        n.createNewMessage(message);
                        MESSAGE_DATABASE.set(i, n);
                        return true;
                    }
                }
                MESSAGE_DATABASE.add(new Message(user1, user2, message));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }

    public User getUser(String username) {
        return USER_DATABASE.get(getUserIndex(username));
    }


    public boolean userExists(String username) {
        synchronized (SYNC_LOCK) {
            for (int i = 0; i < USER_DATABASE.size(); i++) {
                if (USER_DATABASE.get(i).getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setBio(String username, String bio) {
        synchronized (SYNC_LOCK) {
            User newUser = USER_DATABASE.get(getUserIndex(username));
            newUser.setBio(bio);
            USER_DATABASE.set(getUserIndex(username), newUser);
        }
    }
    public String getBio(String username) {
        synchronized (SYNC_LOCK) {
            return USER_DATABASE.get(getUserIndex(username)).getBio();
        }
    }

    //TODO: Readme/go over logic of new methods below
    // --> I think all need to be synchronized as they access the private variables
    //     but maybe not as they don't mutilate them
    //     Reasoning(ideally find a way to not synchronize to maximize efficiency):
    //      - may need to change foreach loops to iterations b/c if they are not synchronized and another thread mutilates the list, idk how enhanced for loop will react
    //      - also don't know if results of normal for loops will be screwed up if the list is mutilated by another thread during iteration(possible out of bounds or some bs)
    public ArrayList<String> getMessageArray(String username, String username2) { //this one may have been documented and gone over already
        synchronized (SYNC_LOCK){
            for (int i = 0; i < MESSAGE_DATABASE.size(); i++) {
                if (MESSAGE_DATABASE.get(i).getUser1().getUsername().equals(username) && MESSAGE_DATABASE.get(i).getUser2().getUsername().equals(username2)) {
                    return MESSAGE_DATABASE.get(i).getMessages();
                }
                if (MESSAGE_DATABASE.get(i).getUser2().getUsername().equals(username) && MESSAGE_DATABASE.get(i).getUser1().getUsername().equals(username2)) {
                    return MESSAGE_DATABASE.get(i).getMessages();
                }
            }
            return null;
        }
    }

    public ArrayList<String> getUsersITalkTo(String username) {
        synchronized (SYNC_LOCK) {
            ArrayList<String> userList = new ArrayList<String>();

            for (Message message : MESSAGE_DATABASE) {
                if (message.getUser1().getUsername().equals(username)) {
                    userList.add(message.getUser2().getUsername());
                }
                if (message.getUser2().getUsername().equals(username)) {
                    userList.add(message.getUser1().getUsername());
                }
            }
            return userList;
        }
    }

    public void deleteMessage(String user1, String user2, int index) {
        this.getMessageArray(user1, user2).remove(index);
    }

    public ArrayList<String> searchUser(String username) {
        ArrayList<String> usersFound = new ArrayList<>();
        synchronized (SYNC_LOCK) {
            for (User user : USER_DATABASE) {
                if (user.getUsername().contains(username)) {
                    usersFound.add(user.getUsername());
                }
            }
        }
        return usersFound;
    }
}

