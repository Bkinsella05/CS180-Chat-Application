package org.example;

import java.util.ArrayList;

public interface MessageInterface {
    public void createNewMessage(String messageLine); //Creates a message from the user
    public boolean removeMessage(int index); //Deletes the message in the arraylist at the index and returns whether it was
    public String readMessage(int index); // returns the message in the arraylist at the index (format "User:Message..")
    public ArrayList<String> getMessages(); //returns the messages arraylist
    public User getUser1(); //returns the first user in the chat
    public User getUser2(); //returns the second user in the chat
    public String toString(); //converts the message to the string form (used in saving to file)
    public boolean equals(Message message); //Checks if the two message are of the same users.
    //Message will be of the format message`message` ...
    //Each message will be formatted username:content

    /* Nth index of, imported from apache commons */
    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
}

