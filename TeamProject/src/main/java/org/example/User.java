package org.example;

import java.util.ArrayList;
/**
 * User
 * A class that sets up the object of each user of our program, containing their personal information and friends/blocked
 *
 * @author Hayden Raffieed, Stephen Shirmeyer, Robert Kinsella, Avneet Kaur Anand
 *
 * @date 12/8/24
 *
 */
public class User implements UserInterface {
    private String username;
    private String password;
    private String bio;
    private ArrayList<String> friends;
    private ArrayList<String> blockedUsers;
    public User(String username, String password) {//constructor for making new users
        this.username = username;
        this.password = password;
        this.bio = "";
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
    }
    public User(String username, String password, String bio, ArrayList<String> friends, ArrayList<String> blockedUsers) { //constructor for making objects of previously made users in files
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.friends = friends;
        this.blockedUsers = blockedUsers;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBio() {
        return bio;
    }
    public ArrayList<String> getFriends() {
        return friends;
    }
    public ArrayList<String> getBlockedUsers() {
        return blockedUsers;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
    public void setBlockedUsers(ArrayList<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }
    //All above methods are simple getters and setters

    public String addFriend(User user) { //See relevant comment in Interface
        try {
            if (friends.contains(user.username)) {
                return "This user is already a friend!";
            }
            else if (this.isBlocked(user)) {
                throw new BlockedUserException("This user is blocked!");
            }
            else { //Instead of what's currently commented we will add a call to the
                // server to verify user exists
                /*if(DatabaseInterface.userExists(user)) { */
                this.friends.add(user.username);
                return "User Added!";
                /*}
                return "User not found";*/
            }
        } catch (BlockedUserException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Input Error";
        }
    }
    public String removeFriend(User user) { //See relevant comment in Interface
        try {
            for (int i = 0; i < friends.size(); i++) {
                if (friends.get(i).equals(user.username)) {
                    friends.remove(i);
                    return "Friend Removed";
                }
            }
            return "Friend not found";
        } catch (Exception e) {
            return "Input Error";
        }
    }
    public boolean isFriend(User user) { //See relevant comment in Interface
        try {
            for (String u : this.friends) {
                if (u.equals(user.username)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public String blockUser(User user) { //See relevant comment in Interface
        try {
            if(blockedUsers.contains(user.username)) {
                throw new BlockedUserException("This user is already blocked!");
            }
            //Instead of what's currently commented we will add a call to the
            // server to verify user exists
            else /*if(DatabaseInterface.userExists(user)) */{
                friends.remove(user.username);
                this.blockedUsers.add(user.username);
                return "User Blocked!";
            }/*
            else {

                return "User not found";
            }*/
        } catch (BlockedUserException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Input Error";
        }
    }
    public String unblockUser(User user) { //See relevant comment in Interface
        try {
            for (int i = 0; i < blockedUsers.size(); i++) {
                if (blockedUsers.get(i).equals(user.username)) {
                    blockedUsers.remove(i);
                    return "User Unblocked!";
                }
            }
            return "User not blocked";
        } catch (Exception e) {
            return "Input Error";
        }
    }
    public boolean isBlocked(User user) {//See relevant comment in Interface
        try {
            for (String u : this.blockedUsers) {
                if (u.equals(user.username)) {
                    return true;
                }
            }
            for (String u : user.blockedUsers) {
                if (u.equals(this.username)) {
                    return true;
                }
            }
            return false;
        } catch(Exception e) {
            return false;
        }

    }
    public boolean equals(User u) { //See relevant comment in Interface
        try {
            return this.username.equals(u.username);
        } catch (Exception e) {
            return false;
        }
    }
    public String toString() { //See relevant comment in Interface
        try {
            String friend = "";
            String blockedUser = "";
            for (String f : friends) {
                friend += ":" + f + "|";
            }
            for (String b : blockedUsers) {
                blockedUser += ":" + b + "|";
            }
            return username + "," + password + "," + bio + "," + friend + "," + blockedUser;
        } catch (Exception e) {
            return "";
        }

    }
    //username,password,bio,friend1|friend2|friend3|,blocked1:blocked2:blocked3:
}
