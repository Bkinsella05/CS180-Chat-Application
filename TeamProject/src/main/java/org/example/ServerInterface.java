package org.example;

public interface ServerInterface {
    public void updateOtherUser(String receiver, Object[] otherClientInputs); //Whenever someone sends a message, using the other persons username
    //if they are online, find their socket and add the message to their GUI. else do nothing.
}
