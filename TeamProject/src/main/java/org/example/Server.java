package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Server
 * The program that hosts all clients through sockets. Does the computing by using MediaDatabase class methods.
 *
 * @author Hayden Raffieed, Stephen Shirmeyer, Robert Kinsella, Avneet Kaur Anand
 *
 * @date 12/8/24
 *
 */
public class Server implements Runnable, ServerInterface {
    private static final MediaDatabase database = new MediaDatabase();
    private static ServerSocket serverSocket;
    private final static ArrayList<Object[]> servers = new ArrayList<Object[]>(); //the first object in the array is the server object, the second in the array is a username as a signature to know which client it is connected with
    private final static Object o = new Object();


    //Now we have a method called update that accesses the shared database to update the intended client object
    //When login occurs, an array with this server object,

    public Server() throws IOException {
    }

    public void run() {
        try (Socket socket = serverSocket.accept()) {//Waits for client to connect and if connection is bad, will re loop back in catch statement to try again
            Object[] thisServer = new Object[2];
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Server threadObject = new Server();
            Thread thread = new Thread(threadObject);
            thread.start(); //starts new thread so more clients can connect

            boolean exit = false;
            String name = "";
            String password;
            String success;
            String status;

            do { //loops based on user gui selections and listens until the client exits
                try {
                    Object[] clientInput = (Object[]) ois.readObject();
                    Object[] objectSend = new Object[]{"This command was not a switch case."};
                    String selection = (String) clientInput[0];
                    switch (selection) {
                        case "Register":
                            //[String name, String password]
                            name = (String) clientInput[1];
                            password = (String) clientInput[2];
                            if (name.isEmpty() || password.isEmpty()) {
                                objectSend = new String[]{"Register", "Error: Blank User or Password", "", ""};
                                break;
                            }
                            success = database.createNewUser(name, password);
                            objectSend = new String[]{"Register", success, name};
                            if (success.contains("Successfully")) {
                                objectSend = new String[]{"Register", success, name, database.getBio(name)};
                                thisServer = new Object[]{name, oos};
                                servers.add(thisServer);
                            }
                            //Need to get the user that is created, set it to this server thread's local user
                            break;
                        case "Login":
                            try {
                                name = (String) clientInput[1];
                                password = (String) clientInput[2];
                                objectSend = new String[]{"Login", "Error: Invalid Credentials"};
                                if (database.userExists(name) || database.getUsers().get(database.getUserIndex(name)).getPassword().equals(password)) {
                                    objectSend = new String[]{"Login", "Successfully Logged In", name, database.getBio(name)};
                                    thisServer = new Object[]{name, oos};
                                    servers.add(thisServer); //Possibly need to change this
                                }
                            } catch (Exception e) {
                                objectSend = new String[]{"Login", "Error: User doesn't Exist"};
                            }
                            break;
                        case "update my bio":
                            String biography = (String) clientInput[1];
                            if (biography.contains(",") || (biography.length() > 120)) {
                                objectSend = new String[]{"bio error", "Error: Invalid Bio: Longer than 120 Characters or Contains a Comma"};
                                break;
                            }
                            database.setBio(name, biography);
                            objectSend = new String[]{"My User Info", name, database.getUser(name).getBio()};
                            break;
                        //TODO: UPDATE README AND CHECK WITH GROUP ON LOGIC
                        //Left side lists:
                        case "Send who I've talked to plz :)":
                            String[] messagees = database.getUsersITalkTo(name).toArray(new String[database.getUsersITalkTo(name).size()]);
                            objectSend = new Object[]{"Update User List", messagees};
                            break;
                        case "Find users with this string.":
                            ArrayList<String> people = database.searchUser((String) clientInput[1]);
                            people.remove(name);
                            String[] people2 = people.toArray(new String[people.size()]);
                            objectSend = new Object[]{"Update User List", people2};
                            break;
                        case "Find my friends plz :)":
                            String[] myFriendsArray = database.getUser(name).getFriends().toArray(new String[database.getUser(name).getFriends().size()]);
                            objectSend = new Object[]{"Update User List", myFriendsArray};
                            break;
                        case "Find my haters >:(":
                            String[] myBlockedArray = database.getUser(name).getBlockedUsers().toArray(new String[database.getUser(name).getBlockedUsers().size()]);
                            objectSend = new Object[]{"Update User List", myBlockedArray};
                            break;
                        case "Send my personal info over!":
                            objectSend = new Object[]{"My User Info", name, database.getUser(name).getBio()};
                            break;
                        case "Send their personal info over>:)":
                            objectSend = new Object[]{"Other User Info", database.getUser((String) clientInput[1]).getUsername(), database.getUser((String) clientInput[1]).getBio(), database.getUser(name).isFriend(database.getUser((String) clientInput[1])), database.getUser(name).isBlocked(database.getUser((String) clientInput[1]))};
                            break;
                        case "Send our conversation over plz :)":
                            String[] messageess = database.getUsersITalkTo(name).toArray(new String[database.getUsersITalkTo(name).size()]);
                            ArrayList<String> messages = database.getMessageArray(name, (String) clientInput[1]);
                            String[] messagesArray;
                            if (messages != null) {
                                messagesArray = messages.toArray(new String[messages.size()]);
                            } else {
                                messagesArray = new String[]{""};
                            }
                            objectSend = new Object[]{"update yo messages", messagesArray, messageess, clientInput[1], false};
                            //updateOtherUser((String) clientInput[1], new Object[]{"update yo messages", objectSend[1], database.getUsersITalkTo((String) clientInput[1]).toArray(new String[database.getUsersITalkTo((String) clientInput[1]).size()]), name, true});
                            break;
                        //Dual oos usage in these to update multiple users
                        case "I am sending a message.":
                            String messaga = (String) clientInput[2];
                            if (messaga.length() > 120 || messaga.isEmpty()) {
                                objectSend = new String[]{"Message too long", (String) clientInput[1]};
                                break;
                            }
                            if (!database.getUser(name).isFriend(database.getUser((String) clientInput[1]))) {
                                objectSend = new String[]{"Not Friend Error", (String) clientInput[1]};
                                break;
                            }
                            if (!database.addMessage(database.getUser(name), database.getUser((String) clientInput[1]), messaga)) {
                                objectSend = new String[]{"Message Error", (String) clientInput[1]};
                                break;
                            }

                            String[] messagee = database.getUsersITalkTo(name).toArray(new String[database.getUsersITalkTo(name).size()]);
                            ArrayList<String> message = database.getMessageArray(name, (String) clientInput[1]);
                            String[] convo;
                            if (message != null) {
                                convo = message.toArray(new String[message.size()]);
                            } else {
                                convo = new String[]{""};
                            }
                            objectSend = new Object[]{"update yo messages", convo, messagee, clientInput[1], false};
                            Object[] theOtherClientsInput = new Object[]{"update yo messages", convo, database.getUsersITalkTo((String) clientInput[1]).toArray(new String[database.getUsersITalkTo((String) clientInput[1]).size()]), name, true};
                            updateOtherUser((String) clientInput[1], theOtherClientsInput);
                            break;
                        case "Add my friend please":
                            status = database.getUser(name).addFriend(database.getUser((String) clientInput[1]));
                            String[] friends = database.getUser(name).getFriends().toArray(new String[database.getUser(name).getFriends().size()]);
                            objectSend = new Object[]{"Update Friends", status, (String) clientInput[1], friends, database.getUser(name).isFriend(database.getUser((String) clientInput[1])), database.getUser(name).isBlocked(database.getUser((String) clientInput[1]))};
                            break;
                        case "They are dead to me. Unfriend please :)":
                            status = database.getUser(name).removeFriend(database.getUser((String) clientInput[1]));
                            String[] friend = database.getUser(name).getFriends().toArray(new String[database.getUser(name).getFriends().size()]);
                            objectSend = new Object[]{"Update Friends", status, (String) clientInput[1], friend, database.getUser(name).isFriend(database.getUser((String) clientInput[1])), database.getUser(name).isBlocked(database.getUser((String) clientInput[1]))};
                            break;
                        case "Block this b":
                            status = database.getUser(name).blockUser(database.getUser((String) clientInput[1]));
                            String[] blocked = database.getUser(name).getBlockedUsers().toArray(new String[database.getUser(name).getBlockedUsers().size()]);
                            String[] friendAfterBlocked = database.getUser(name).getFriends().toArray(new String[database.getUser(name).getFriends().size()]);
                            objectSend = new Object[]{"Update Blocked", status, (String) clientInput[1], blocked, database.getUser(name).isFriend(database.getUser((String) clientInput[1])), database.getUser(name).isBlocked(database.getUser((String) clientInput[1])), friendAfterBlocked};
                            break;
                        case "They chill now":
                            status = database.getUser(name).unblockUser(database.getUser((String) clientInput[1]));
                            String[] block = database.getUser(name).getBlockedUsers().toArray(new String[database.getUser(name).getBlockedUsers().size()]);
                            String[] friendAfterBlock = database.getUser(name).getFriends().toArray(new String[database.getUser(name).getFriends().size()]);
                            objectSend = new Object[]{"Update Blocked", status, (String) clientInput[1], block, database.getUser(name).isFriend(database.getUser((String) clientInput[1])), database.getUser(name).isBlocked(database.getUser((String) clientInput[1])), friendAfterBlock};
                            break;
                        case "Exit":
                            exit = true;
                            break;
                        case "Lets remove this...":
                            database.deleteMessage(name, (String) clientInput[1], (int) clientInput[2]);
                            String[] messageee = database.getUsersITalkTo(name).toArray(new String[database.getUsersITalkTo(name).size()]);
                            ArrayList<String> messagess = database.getMessageArray(name, (String) clientInput[1]);
                            String[] convos;
                            if (messagess != null) {
                                convos = messagess.toArray(new String[messagess.size()]);
                            } else {
                                convos = new String[]{""};
                            }
                            objectSend = new Object[]{"update yo messages", convos, messageee, clientInput[1], false};
                            updateOtherUser((String) clientInput[1], new Object[]{"update yo messages", convos, messageee, name, true});
                            break;
                        default:
                            break;
                    }
                    synchronized (o) {
                        oos.writeObject(objectSend);
                        oos.flush();
                    }
                } catch (Exception e) {
                    break;
                }
            } while (!exit);
            oos.close();
            ois.close();
            socket.close();
            database.createMessageFile();
            database.createUserFile();
            servers.remove(thisServer);
            return;
        } catch (Exception e) {
            database.createMessageFile();
            database.createUserFile();
            return;
        }
    }

    //TODO:
    // THIS NEEDS TO SEND EVERYTHING TO THE CLIENT TO MAKE THE MESSAGE WINDOW
    public void updateOtherUser(String receiver, Object[] otherClientInputs) {//will message other clients to update gui upon message sending in order to relay most recent information
        Object[] thisServer = null;
        try {
            for (Object[] server : servers) {
                if (server[0].equals(receiver)) {
                    thisServer = server;
                    break;
                }
            }
            if (thisServer == null) {
                throw new Exception(); //if client is not online, the server will not exist inside servers
            }
            ObjectOutputStream oos = (ObjectOutputStream) thisServer[1];
            oos.writeObject(otherClientInputs);
            oos.flush();
            return;
        } catch (Exception e) {
            //TODO
            return;
        }
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(4500);
            Server obj = new Server();
            Thread thread = new Thread(obj);
            thread.start();
            while (true) {//ensures server main method doesn't stop while threads run, always accepts connections
            }
        } catch (IOException ioe) {
            return;
        }
    }
}
