# **_CS180 Team Project: SINSTAGRAM_**

Group project for Stephen, Hayden, Bobby, and Avneet.

To start the program, first run the `Server` class, then once it's running, start the `Client` class.

This project was built on IntelliJ using Maven architecture. We have all relevant code in the `src` folder, with the project in `main/java/org/example` and the test cases are in `test/java/org/example`.

## Project Details

This project, in many classes, uses the `Ordinal Index Of` method imported from the Apache commons. Its arguments are:

- `String` (the string we're indexing)
- `String` (what we're indexing for)
- `int` (which index we want)

We use it to find the nth index of String 2 in String 1.

### Functionality to keep in mind
- When a user registers a new account they are automatically logged into it
- Messages are to be clicked for them to be deleted.
- User search searches through the entire database of users.
- Clicking on friends or blocked in the drop-down makes the user search results a list of friends/blocked based on what the user clicked.
- Friends have to be added for both users for them to send messages to them. So if user1 has added user2 as a friend, user1 can message user2. But user2 can't reply until they add user1 back as a friend.
- If a user is blocked, then you can't send messages to them. If user1 has blocked user2: user1 can't message user2 and user2 can't message user1

## Exceptions

### BlockedUserException Class
An exception for when the user is blocked, thrown when an action cannot be taken due to said block.

## Interfaces

### DatabaseInterface

The interface for `MediaDatabase`. Holds:

- `Ordinal Index Of` (top of README)
- `String USERFILENAME`, `String MESSAGEFILENAME` - The String File names

The non-defined public methods in the interface are:

- `boolean userExists(String username)` // Returns if the user exists in array of users.
- `boolean createOldUsers()` // Reads the messages in the file "MessageFileNames.txt" and adds them to the message ArrayList.
- `boolean createOldMessages()` // Reads the user information in "UserFileNames.txt" and adds them to the user ArrayList.
- `int getUserIndex(String username)` // Gets the index of a user with username 'username' in the arraylist of users.
- `int getUserIndex(User user)` // Gets the index of a user in the arraylist of users.
- `boolean createUserFile()` // Creates the username file for when we need persistent data.
- `boolean createMessageFile()` // Creates the message file for when we need persistent data.
- `ArrayList<User> getUsers()` // Returns arraylist of users.
- `ArrayList<Message> getMessages()` // Returns an arraylist of message conversations.
- `boolean addMessage(Message message)` // Checks to see if the method contains separation characters and then finds the message object and if it exists it calls addMessage from the message class, otherwise it makes a new Message object.
- `boolean deleteMessage(Message message)` // Deletes message from object after checking to see it exists.
- `User getUser(String username)` // Returns the user object with the matching name of the string input.
- `boolean containsIndexingCharacters(String toCheck)` //Returns boolean whether or not string has separation characters

### MessageInterface

The interface for the `Message` class, holds `Ordinal Index Of` and empty public methods:

- `void createNewMessage(User user)` // Creates a message from the user.
- `boolean removeMessage(int index)` // Deletes the message in the arraylist at the index and returns whether it was removed.
- `void readMessage()` // Returns the message in the arraylist at the index (format "User:Message...").
- `ArrayList<String> getMessages()` // Returns the messages arraylist.
- `User getUser1()` // Returns the first user in the chat.
- `User getUser2()` // Returns the second user in the chat.
- `String toString()` // Converts the message to the string form (used in saving to file).
- `boolean equals(Message message)` // Checks if the two messages are of the same users.

### UserInterface

The interface for the `User` class, holds getters for all variables and setters for all but the username variable. Also, the empty public methods:

- `String addFriend(User user)` // Gets user list, goes through friend list and makes sure they aren't already your friend. If not, make sure user is a valid user and add them to friend list.
- `String blockUser(User user)` // If user isn't already blocked and user exists, blocks a user, they can no longer send or be sent messages to/from the blocked user and returns user blocked, else returns user not found or user already blocked.
- `String removeFriend(User user)` // Removes the user from your friends list if they are already your friend and they exist, returns a statement accordingly.
- `String unblockUser(User user)` // Removes the user from your block list.
- `boolean isFriend(User user)` // Checks if a user is on this user's friend list.
- `boolean isBlocked(User user)` // Checks if a user is on this user's block list.
- `String toString()` // Converts the user to the string form (used in saving to file).
  - toString example: `Username,Password,This is my bio,friend1|friend2,blocked1:blocked2:blocked3`
- `boolean equals(User user)` // Checks if this user is equal to the passed user.

### ClientInterface

Client interface is empty as all the methods in the client class are static.

### ServerInterface

The server interface holds the empty public method:

- `void updateOtherUser(String username, String sender)`: Whenever someone sends a message, using the other person's username, if they are online, find their socket and add the message to their GUI. Otherwise, do nothing.

## Classes

### Client Class

The `Client` class implements the user interface and interaction for the social media application.

#### Variables:
- `JTextField username`
- `JTextField password`
- `JFrame frame`
- `Socket socket`
- `ObjectOutputStream oos`
- `JPanel login`
- `JPanel register`
- `Container content`
- `JPanel userPanel`
- `JPanel bioPanel`
- `JPanel buttonPanel`
- `JPanel textBoxToSendMessagePanel`
- `JComboBox<String> dropDown`
- `JPanel leftScrollPanel`
- `JPanel rightPanel`
- `JPanel resultsPanel`
- `JLabel userGettingMessagedLabel`
- `JPanel messageWrapperBox`

The methods of the `Client` class handle the GUI for login, registration, viewing user profiles, sending and receiving messages, and managing friends and blocked users.
#### Client Class Methods

##### `registerOrLogin(Container content)`
Displays the GUI for user selection between registering or logging in.

##### `loginButtonClicked(Container content)`
Displays the GUI for the login screen.

##### `registerButtonClicked(Container content)`
Displays the GUI for the registration screen.

##### `runAfterLogin(Container c, String user, String bio)`
Sets up the main GUI after the user has logged in, including the layout for the user's profile, friends, messages, and search functionality.

##### `displayResults(JPanel scrollPanel, ArrayList<String> users)`
Displays the search results for users in a scrollable panel.

##### `messageBox(String user, String[] messages, boolean isOtherServer)`
Displays the chat interface with the specified user, including the conversation and input field for sending new messages.

##### `bigWhiteBoxOtherUserView(String user, String bio, boolean isBlocked, boolean isFriend)`
Displays the profile view of another user, including their bio and options to block/unblock and add/remove them as a friend.

##### `updateButtons(String person, boolean isFriend, boolean isBlocked)`
Updates the buttons for blocking/unblocking and adding/removing friends based on the user's relationship status with the specified person.

##### `bigWhiteBoxMyView(String user, String bio)`
Displays the logged-in user's profile view, including their bio and an option to edit it.

##### `run()`
Starts the main application, setting up the initial frame and displaying the login or registration options.

##### `getMessageUsernames()`
Requests the list of users that the logged-in user has communicated with from the server.

##### `getFriendUsernames()`
Requests the list of friends of the logged-in user from the server.

##### `getBlockedUsernames()`
Requests the list of blocked users of the logged-in user from the server.

##### `getMyUser()`
Requests the logged-in user's personal information from the server.

##### `setMyUserInfo(String info)`
Sends a request to the server to update the logged-in user's bio with the specified information.

##### `getUser(String username)`
Requests the personal information of a specified user from the server.

##### `getMessageHistory(String username)`
Requests the message history between the logged-in user and the specified user from the server.

##### `sendMessage(String usernameToSendTo, String message)`
Sends a message to the specified user through the server.

##### `addFriend(String username)`
Sends a request to the server to add the specified user as a friend.

##### `removeFriend(String username)`
Sends a request to the server to remove the specified user from the friends list.

##### `blockUser(String username)`
Sends a request to the server to block the specified user.

##### `unblockUser(String username)`
Sends a request to the server to unblock the specified user.

##### `searchUser(String username)`
Sends a request to the server to search for users with usernames containing the specified string.

##### `deleteMessage(String username, int index)`
Sends a request to the server to delete a message at the specified index in the conversation with the specified user.

##### `main(String[] args)`
The main method to start the client application, establishing a connection to the server and initiating the GUI.


### Server Class

The `Server` class handles the backend server operations, including managing user data, messages, and client connections.

#### Variables:
- `MediaDatabase database`
- `ServerSocket serverSocket`
- `ArrayList<Object[]> servers`
- `Object o`

The methods of the `Server` class handle user registration and login, updating user information, managing friends and blocked lists, and relaying messages between clients.

#### Server Class Methods

##### `run()`
Handles client connections and manages interactions with the database. Listens for incoming client connections and processes their requests.

##### `updateOtherUser(String receiver, Object[] otherClientInputs)`
Sends updates to other clients when a message is sent, ensuring the most recent information is relayed.

##### `updateOtherFriend(String friendee, Object[] otherClientInputs)`
Sends updates to other clients when the friend list changes, ensuring the most recent information is relayed.

##### `main(String[] args)`
Starts the server, accepting client connections and initiating the necessary threads.

### Message Class

The `Message` class handles the structure and storage of messages between users.

#### Variables:
- `ArrayList<String> messages` (in the format of "User1: blahblah" "User2: blahblah")
- `User user1`
- `User user2`

The methods of the `Message` class handle creating new messages, removing messages, reading messages, and converting messages to and from string format.

#### Message Class Methods

##### `Message(User user1, User user2, String message)`
Constructor for creating a `Message` object from the server file. Parses the message string and initializes the message list.

##### `Message(User user1, User user2)`
Constructor for creating a new chat between two users. Throws a `BlockedUserException` if either user is blocked by the other.

##### `createNewMessage(String messageLine)`
Adds a new message to the conversation, provided neither user is blocked by the other.

##### `removeMessage(int index)`
Deletes the message at the specified index in the message list. Returns `true` if the message is successfully removed, otherwise `false`.

##### `readMessage(int index)`
Returns the message at the specified index in the message list. Returns an empty string if the index is out of bounds.

##### `getMessages()`
Returns the message list for the conversation.

##### `getUser1()`
Returns the first user in the chat.

##### `getUser2()`
Returns the second user in the chat.

##### `toString()`
Converts the message list to a string format suitable for saving to a file.

##### `equals(Message message)`
Checks if two `Message` objects are equivalent, i.e., they have the same users.

### MediaDatabase Class

The `MediaDatabase` class manages the persistent storage and retrieval of user and message data.

#### Variables:
- `ArrayList<User> USER_DATABASE` - The ArrayLists of the databases.
- `ArrayList<Message> MESSAGE_DATABASE`
- `Object SYNC_LOCK` - the SyncLock for synchronizing when we change/access relevant variables.

The methods of the `MediaDatabase` class handle creating and updating user and message files, checking for existing users, adding and removing messages, and searching for users.

#### MediaDatabase Class Methods

##### `createOldUsers()`
Reads the user information from the file and adds them to the user ArrayList. Returns `true` if successful, otherwise `false`.

##### `createOldMessages()`
Reads the messages from the file and adds them to the message ArrayList. Returns `true` if successful, otherwise `false`.

##### `createNewUser(String username, String password)`
Creates a new user with the given username and password. Returns a message indicating whether the user was created successfully or if there was an error.

##### `createUserFile()`
Creates or overwrites the user file with the updated user information. Returns `true` if successful, otherwise `false`.

##### `createMessageFile()`
Creates or overwrites the message file with the updated message information. Returns `true` if successful, otherwise `false`.

##### `getUserIndex(String username)`
Returns the index of the user with the specified username in the user ArrayList. Returns `-1` if the user does not exist.

##### `getUserIndex(User user)`
Returns the index of the specified user object in the user ArrayList. Returns `-1` if the user does not exist.

##### `getUsers()`
Returns the ArrayList of users.

##### `getMessages()`
Returns the ArrayList of messages.

##### `addMessage(User user1, User user2, String message)`
Adds a new message to the conversation between the specified users. Returns `true` if successful, otherwise `false`.

##### `getUser(String username)`
Returns the user object with the specified username.

##### `userExists(User user)`
Checks if the specified user object exists in the user ArrayList. Returns `true` if the user exists, otherwise `false`.

##### `userExists(String username)`
Checks if the user with the specified username exists in the user ArrayList. Returns `true` if the user exists, otherwise `false`.

##### `setBio(String username, String bio)`
Sets the bio for the user with the specified username.

##### `getBio(String username)`
Returns the bio of the user with the specified username.

##### `getMessageArray(String username, String username2)`
Returns the message list for the conversation between the specified users.

##### `getUsersITalkTo(String username)`
Returns an ArrayList of usernames that the specified user has communicated with.

##### `getUserMessages(String username)`
Returns an ArrayList of message lists for the specified user.

##### `deleteMessage(String user1, String user2, int index)`
Deletes the message at the specified index in the conversation between the specified users.

##### `searchUser(String username)`
Searches for users whose usernames contain the specified string. Returns an ArrayList of matching usernames.

### User Class

The `User` class handles the structure and storage of user data, including friends and blocked lists.

#### Variables:
- `String username`
- `String password`
- `String bio`
- `ArrayList<String> friends`
- `ArrayList<String> blockedUsers`

The methods of the `User` class handle getting and setting user attributes, adding and removing friends, blocking and unblocking users, and converting user data to and from string format.

#### User Class Methods

##### `getUsername()`
Returns the username of the user.

##### `getPassword()`
Returns the password of the user.

##### `getBio()`
Returns the bio of the user.

##### `getFriends()`
Returns the list of friends of the user.

##### `getBlockedUsers()`
Returns the list of blocked users of the user.

##### `setPassword(String password)`
Sets the password of the user.

##### `setBio(String bio)`
Sets the bio of the user.

##### `setFriends(ArrayList<String> friends)`
Sets the list of friends of the user.

##### `setBlockedUsers(ArrayList<String> blockedUsers)`
Sets the list of blocked users of the user.

##### `addFriend(User user)`
Adds a friend to the user's friend list. Returns a message indicating whether the user was added successfully or if there was an error.

##### `removeFriend(User user)`
Removes a friend from the user's friend list. Returns a message indicating whether the friend was removed successfully or if there was an error.

##### `isFriend(User user)`
Checks if the specified user is in the user's friend list. Returns `true` if they are friends, otherwise `false`.

##### `blockUser(User user)`
Blocks the specified user. Removes them from the friend list if they are friends. Returns a message indicating whether the user was blocked successfully or if there was an error.

##### `unblockUser(User user)`
Unblocks the specified user. Returns a message indicating whether the user was unblocked successfully or if there was an error.

##### `isBlocked(User user)`
Checks if the specified user is in the user's blocked list or if the user is blocked by the specified user. Returns `true` if they are blocked, otherwise `false`.

##### `equals(User u)`
Checks if the specified user object is equal to the current user object based on the username. Returns `true` if they are equal, otherwise `false`.

##### `toString()`
Converts the user object to a string format suitable for saving to a file.

## Test Cases

NOTE 1: We don't have interface test cases as we decided the current scope of the class tests were broad enough to forgo the interfaces.

NOTE 2: We don't have a BlockedUserException test case as the addFriend and blockUser test cases of the UserTestClass create a BlockedUserException.

### MessageTestClass Overview

JUnit test class for verifying the `Message` class. It tests functionalities related to message creation, reading, adding, and removing messages, as well as handling blocked users.

#### Methods Tested:

##### `testConstructorWithMessage()`
Tests the constructor of the `Message` class with a message string to ensure it initializes the message list correctly.

##### `testConstructorBlockedUser()`
Tests the constructor of the `Message` class for handling blocked users by throwing a `BlockedUserException`.

##### `testCreateNewMessage()`
Tests the `createNewMessage` method to ensure new messages are added to the message list correctly.

##### `testCreateNewMessageBlocked()`
Tests the `createNewMessage` method to ensure no new messages are added if one user has blocked the other.

##### `testRemoveMessage()`
Tests the `removeMessage` method to ensure messages are correctly removed from the message list.

##### `testRemoveMessageNotPresent()`
Tests the `removeMessage` method to ensure no changes are made if the specified message is not present.

##### `testReadMessageValidIndex()`
Tests the `readMessage` method to ensure the correct message is returned for a valid index.

##### `testReadMessageInvalidIndex()`
Tests the `readMessage` method to ensure an empty string is returned for an invalid index.

##### `testGetMessages()`
Tests the `getMessages` method to ensure the message list is returned correctly.

##### `testToString()`
Tests the `toString` method to ensure the message list is correctly converted to a string format.


### ClientTestClass Overview:

JUnit test class for verifying the `Client` class. It tests the existence of specific methods required for user interactions such as registering, logging in, and updating the user interface.

#### Methods Tested:

##### `testRegisterOrLoginMethodExists()`
Verifies the existence of the `registerOrLogin` method.

##### `testLoginButtonClickedMethodExists()`
Verifies the existence of the `loginButtonClicked` method.

##### `testRegisterButtonClickedMethodExists()`
Verifies the existence of the `registerButtonClicked` method.

##### `testRunMethodExists()`
Verifies the existence of the `run` method.

##### `testMainMethodExists()`
Verifies the existence of the `main` method.

##### `testRunAfterLoginMethodExists()`
Verifies the existence of the `runAfterLogin` method.

##### `testDisplayResultsMethodExists()`
Verifies the existence of the `displayResults` method.

##### `testMessageBoxMethodExists()`
Verifies the existence of the `messageBox` method.

##### `testBigWhiteBoxOtherUserViewMethodExists()`
Verifies the existence of the `bigWhiteBoxOtherUserView` method.

##### `testUpdateButtonsMethodExists()`
Verifies the existence of the `updateButtons` method.

##### `testBigWhiteBoxMyViewMethodExists()`
Verifies the existence of the `bigWhiteBoxMyView` method.

##### `testGetMessageUsernamesMethodExists()`
Verifies the existence of the `getMessageUsernames` method.

##### `testGetFriendUsernamesMethodExists()`
Verifies the existence of the `getFriendUsernames` method.

##### `testGetBlockedUsernamesMethodExists()`
Verifies the existence of the `getBlockedUsernames` method.

##### `testGetMyUserMethodExists()`
Verifies the existence of the `getMyUser` method.

##### `testSetMyUserInfoMethodExists()`
Verifies the existence of the `setMyUserInfo` method.

##### `testGetUserMethodExists()`
Verifies the existence of the `getUser` method.

##### `testGetMessageHistoryMethodExists()`
Verifies the existence of the `getMessageHistory` method.

##### `testSendMessageMethodExists()`
Verifies the existence of the `sendMessage` method.

##### `testAddFriendMethodExists()`
Verifies the existence of the `addFriend` method.

##### `testRemoveFriendMethodExists()`
Verifies the existence of the `removeFriend` method.

##### `testBlockUserMethodExists()`
Verifies the existence of the `blockUser` method.

##### `testUnblockUserExists()`
Verifies the existence of the `unblockUser` method.

##### `testSearchUserExists()`
Verifies the existence of the `searchUser` method.

##### `testDeleteMessageExists()`
Verifies the existence of the `deleteMessage` method.

### MediaDatabaseTestClass Overview

JUnit test class for verifying the `MediaDatabase` class. It tests functionalities related to messages, user management, storage, and file operations.

#### Methods Tested:

##### `testCreateUserFile()`
Verifies the existence of the `createUserFile` method.

##### `testCreateMessageFile()`
Verifies the existence of the `createMessageFile` method.

##### `testCreateOldUsers()`
Tests if the method successfully reads old user data from the file and initializes the user list.

##### `createOldMessages()`
Tests if the method successfully reads old messages from the file and initializes the message list.

##### `testCreateNewUser()`
Tests the creation of new users with various valid and invalid inputs. Checks if the correct messages are returned for different scenarios.

##### `testGetUserIndexWithUserName()`
Tests if the method returns the correct index for a given username.

##### `testGetUserIndexWithoutUserName()`
Tests if the method returns the correct index for a given user object.

##### `testAddMessage()`
Tests if messages are correctly added to the message list. Validates the handling of special characters in the message content

### ServerTestClass Overview

JUnit test class for verifying the `Server` class. It tests the existence of specific methods required for server operations and client interactions.

#### Methods Tested:

##### `testRunMethodExists()`
Verifies the existence of the `run` method.

##### `testUpdateOtherUserMethodExists()`
Verifies the existence of the `updateOtherUser` method.

##### `testMainMethodExists()`
Verifies the existence of the `main` method.

### UserTestClass Overview

JUnit test class for verifying the `User` class. It tests functionalities related to user attributes, friends, and blocked users.

#### Methods Tested:

##### `testOne()`
Tests the `toString` method for different user objects to ensure the correct string representation.

##### `testTwo()`
Tests the getters for username, password, bio, friends, and blocked users to ensure they return the correct values.

##### `testThree()`
Tests the getters for username, password, bio, friends, and blocked users for a user object with different values to ensure they return the correct values.

##### `testSetPassword()`
Tests the `setPassword` method to ensure the password is correctly updated.

##### `testSetBio()`
Tests the `setBio` method to ensure the bio is correctly updated.

##### `testSetFriends()`
Tests the `setFriends` method to ensure the friends list is correctly updated.

##### `testSetBlockedUsers()`
Tests the `setBlockedUsers` method to ensure the blocked users list is correctly updated.

##### `testAddFriend()`
Tests the `addFriend` method to ensure a user can add a friend correctly and that the friend is added to the friends list.

##### `testAddFriendAlreadyFriend()`
Tests the `addFriend` method to ensure a user cannot add a friend who is already in their friends list.

##### `testAddFriendBlockedUser()`
Tests the `addFriend` method to ensure a user cannot add a friend who is in their blocked users list.

##### `testRemoveFriend()`
Tests the `removeFriend` method to ensure a user can remove a friend correctly and that the friend is removed from the friends list.

##### `testRemoveFriendNotFound()`
Tests the `removeFriend` method to ensure the correct message is returned
