package org.example;

import java.util.ArrayList;

public interface UserInterface {
    public String getUsername(); //returns the username of the user object
    public String getPassword(); //returns the password of the user object
    public String getBio(); // returns the boi of the user object
    public ArrayList<String> getFriends(); // returns the friends arraylist of strings of names of friends
    public ArrayList<String> getBlockedUsers(); // returns the blocked users arraylist of strings of names of blocked users
    public void setPassword(String password); // sets the password to 'password'
    public void setBio(String bio); // sets the bio to 'bio'
    public void setFriends(ArrayList<String> friends); // sets the friends arraylist to 'friends'
    public void setBlockedUsers(ArrayList<String> blockedUsers); // sets the blocked arraylist to 'blockedUsers'


    public String addFriend(User user); //Gets user list, goes through friend list and makes sure they aren't already your friend.
    // If not, make sure user is a valid user and add them to friend list
    public String blockUser(User user); //if user isn't already blocked and user exists blocks a user, they can no longer send or be sent messages to/from the blocked user
    // and returns user blocked, else returns user not found or user already blocked
    public String removeFriend(User user); //Removes the user from your friends list if they are already your friend and they exist, returns a statement accordingly
    public String unblockUser(User user); //Removes the user from your block list
    public boolean isFriend(User user); //Checks if a user is on this users friend list.
    public boolean isBlocked(User user);//Checks if a user is on this users block list.
    public String toString(); //converts the message to the string form (used in saving to file)
    //toString example: Username,Password,This is my bio,friend1|friend2,blocked1:blocked2:blocked3
    public boolean equals(User user); //Checks if this user is equal to the passed user


}
