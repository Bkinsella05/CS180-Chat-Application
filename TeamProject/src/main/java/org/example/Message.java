package org.example;

import java.util.*;
/**
 * Message
 * this is the class that holds each message objects data
 *
 * @author Hayden Raffieed, Stephen Shirmeyer, Robert Kinsella, Avneet Kaur Anand
 *
 * @date 12/8/24
 *
 */
public class Message implements MessageInterface {
    protected ArrayList<String> messages;
    //messages = <"user1: abcd", "user2: abcd", .....>
    private User user1;
    private User user2;

    //THIS CONSTRUCTOR IS FOR WHEN WE TAKE A MESSAGE FROM THE SERVER FILE
    public Message(User user1, User user2, String message) {
        //Message will be of the format message`message` ...
        //Each message will be formatted username:content
        this.user1 = user1;
        this.user2 = user2;
        messages = new ArrayList<String>();
        int counter = 0;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '`') {
                int index1 = MessageInterface.ordinalIndexOf(message, "`", counter);
                int index2 = MessageInterface.ordinalIndexOf(message, "`", counter + 1);

                if (counter == 0) {
                    counter++;
                    this.messages.add(message.substring(0, index1));
                } else {
                    counter++;
                    String separate = message.substring(index1 + 1, index2);
                    this.messages.add(separate);
                }


            }
        }
    }

    //THIS CONSTRUCTOR IS FOR WHEN WE CREATE A NEW CHAT
    public Message(User user1, User user2) throws BlockedUserException {
        if ((!user1.isBlocked(user2) && !user2.isBlocked(user1))) {
            this.user1 = user1;
            this.user2 = user2;
        } else {
            throw new BlockedUserException("User Blocked: Can't send message");
        }
    }

    public void createNewMessage(String messageLine) { //See relevant comment in MessageInterface
        if (!user1.isBlocked(user2) && !user2.isBlocked(user1)) {
            this.messages.add(messageLine);
        }
    }

    public boolean removeMessage(int index) { //See relevant comment in MessageInterface
        //Deletes the message in the arraylist at the index
        try {
            this.messages.remove(index);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public String readMessage(int index) { //See relevant comment in MessageInterface
        // returns the message in the arraylist at the index (format "User:Message..")
        try {
            return this.messages.get(index);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public ArrayList<String> getMessages() { //See relevant comment in MessageInterface
        //returns the messages arraylist
        return this.messages;
    }

    public User getUser1() { //See relevant comment in MessageInterface
        return this.user1;
    }

    public User getUser2() { //See relevant comment in MessageInterface
        return this.user2;
    }

    public String toString() { //See relevant comment in MessageInterface
        String str = "";
        for (int i = 0; i < this.messages.size(); i++) {

            str = str + this.messages.get(i) + "`";
        }
        return user1.getUsername() + "|" + user2.getUsername() + "|" + str;
    }

    public boolean equals(Message message) {
        if ((message.getUser1().equals(this.user1) && message.getUser2().equals(this.user2)) || (message.getUser1().equals(this.user2) && message.getUser2().equals(this.user1))) {
            return true;
        }
        return false;
    }
    //Full messages look like (User1|User2|Messages)
    //Messages will be of the format message`message` ...
    //Each message will be formatted username:content
    //
}
