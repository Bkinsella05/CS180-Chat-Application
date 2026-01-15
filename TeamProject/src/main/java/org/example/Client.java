package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Client
 * This is the class that holds all the GUIs and the communication with the server.
 *
 * @author Hayden Raffieed, Stephen Shirmeyer, Robert Kinsella, Avneet Kaur Anand
 * @date 12/8/2024
 */
public class Client implements Runnable, ClientInterface {
    private static JTextField username;
    private static JTextField password;
    private static JFrame frame;
    private static ObjectOutputStream oos;
    private static JPanel login;
    private static JPanel register;
    private static Container content;
    private static JPanel userPanel;
    private static JPanel bioPanel;
    private static JPanel buttonPanel;
    private static JPanel textBoxToSendMessagePanel;
    private static JComboBox<String> dropDown;
    private static JPanel leftScrollPanel;
    private static JPanel rightPanel;
    private static JPanel resultsPanel = new JPanel(new BorderLayout());
    private static JLabel userGettingMessagedLabel = new JLabel();
    private static JPanel messageWrapperBox = new JPanel(new BorderLayout());
    private static final Object o = new Object();
    //Need private variables for:
    // - Panel with this users bio
    // - Panel on the other users bio
    // - Panel with messages

    public static void registerOrLogin(Container content) {
        //Displays the GUI for user selection between registering or logging in
        JPanel loginQ = new JPanel(new GridBagLayout());
        JLabel loginOrRegister = new JLabel("Would you like to register or login?");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        loginQ.add(loginOrRegister, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        loginQ.add(loginButton, constraints);

        constraints.gridx = 1;
        loginQ.add(registerButton, constraints);

        content.add(loginQ);
        content.revalidate();
        content.repaint();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginQ.setVisible(false);
                loginButtonClicked(content);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginQ.setVisible(false);
                registerButtonClicked(content);
            }
        });
    }

    public static void loginButtonClicked(Container content) {
        //Displays the GUI for the login screen
        login = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        final JLabel usernamePrompt = new JLabel("Username:");
        login.add(usernamePrompt, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        username = new JTextField(16);
        login.add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        final JLabel passwordPrompt = new JLabel("Password:");
        login.add(passwordPrompt, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        password = new JTextField(16);
        login.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton enterButton = new JButton("Enter");
        login.add(enterButton, gbc);

        content.add(login, BorderLayout.CENTER);

        content.revalidate();
        content.repaint();

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userStr = username.getText();
                String passStr = password.getText();
                String[] send = new String[]{"Login", userStr, passStr};
                try {
                    synchronized (o) {
                        oos.writeObject(send);
                        oos.flush();
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Could Not Login",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    login.setVisible(false);
                    registerOrLogin(content);
                    return;
                }
            }
        });
    }

    public static void registerButtonClicked(Container content) {
        //Displays the GUI for the registration screen
        register = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        final JLabel usernamePrompt = new JLabel("Username:");
        register.add(usernamePrompt, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        username = new JTextField(16);
        register.add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        final JLabel passwordPrompt = new JLabel("Password:");
        register.add(passwordPrompt, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        password = new JTextField(16);
        register.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton enterButton = new JButton("Enter");
        register.add(enterButton, gbc);

        content.add(register, BorderLayout.CENTER);

        content.revalidate();
        content.repaint();


        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userStr = username.getText();
                String passStr = password.getText();
                String[] send = new String[]{"Register", userStr, passStr};
                try {
                    synchronized (o) {
                        oos.writeObject(send);
                        oos.flush();
                    }
                } catch (IOException ez) {
                    register.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Could Not Register",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    register.setVisible(false);
                    registerOrLogin(content);
                    return;
                }
            }
        });
    }

    public static void runAfterLogin(Container c, String user, String bio) {
        c.setLayout(new BorderLayout(10, 10)); // Add gaps between regions
        //Create the wrapper panels for all parts of the GUI after login
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        topPanel.setPreferredSize(new Dimension(c.getWidth(), 100));
        c.add(topPanel, BorderLayout.NORTH);

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setPreferredSize(new Dimension(300, c.getHeight()));
        leftWrapper.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel leftSearchPanel = new JPanel();
        leftSearchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        leftSearchPanel.setPreferredSize(new Dimension(300, 100));
        leftPanel.add(leftSearchPanel);

        leftScrollPanel = new JPanel(new BorderLayout());
        leftScrollPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        leftPanel.add(leftScrollPanel);

        leftWrapper.add(leftPanel, BorderLayout.CENTER);
        c.add(leftWrapper, BorderLayout.WEST);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        c.add(rightPanel, BorderLayout.CENTER);

        //Create the left top panel that has the name and eventually picture
        JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel Sinstagram = new JLabel("Sinstagram");
        Sinstagram.setFont(new Font("JetBrains Mono", Font.BOLD, 45));
        leftTopPanel.add(Sinstagram); // Add label to the left panel
        topPanel.add(leftTopPanel, BorderLayout.WEST);
        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dropDown = new JComboBox<>();
        dropDown.addItem("View My Profile");
        dropDown.addItem("Messages");
        dropDown.addItem("Blocked");
        dropDown.addItem("Friends");
        dropDown.addItem("Log Out");
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                JComboBox comboBox = (JComboBox) event.getSource();

                Object selected = comboBox.getSelectedItem();
                if (selected.toString().equals("View My Profile")) {
                    getMyUser();
                } else if (selected.toString().equals("Messages")) {
                    getMessageUsernames();
                } else if (selected.toString().equals("Blocked")) {
                    getBlockedUsernames();
                } else if (selected.toString().equals("Friends")) {
                    getFriendUsernames();
                } else if (selected.toString().equals("Log Out")) {
                    c.removeAll();
                    c.revalidate();
                    c.repaint();
                    registerOrLogin(content);
                }
            }
        });
        rightTopPanel.add(dropDown);
        topPanel.add(rightTopPanel, BorderLayout.EAST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        JTextField searchTextField = new JTextField(20);
        searchPanel.add(searchTextField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchTextField.getText();
                searchUser(query);
            }
        });
        searchPanel.add(searchButton, BorderLayout.EAST);
        leftSearchPanel.add(searchPanel, BorderLayout.WEST);
        getMyUser();

        c.revalidate();
        c.repaint();
    }

    public static void displayResults(JPanel scrollPanel, ArrayList<String> users) {
        resultsPanel.removeAll();
        JScrollPane scroll = new JScrollPane();
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.removeAll();
        for (String user : users) {
            JButton userButton = new JButton(user);
            userButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            userButton.setMaximumSize(new Dimension((int) (scrollPanel.getWidth() * .9), 50));
            userButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getUser(user);
                }
            });
            buttons.add(userButton);
        }
        buttons.setPreferredSize(new Dimension((int) (scrollPanel.getWidth() * .9), users.size() * 50));

        scroll.setViewportView(buttons);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        resultsPanel.add(scroll, BorderLayout.CENTER);

        scrollPanel.removeAll();
        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.add(resultsPanel, BorderLayout.CENTER);

        content.revalidate();
        content.repaint();
    }

    public static void messageBox(String user, String[] messages, boolean isOtherServer) {

        if (!isOtherServer) {
            rightPanel.removeAll();
            textBoxToSendMessagePanel = new JPanel(new FlowLayout());
            JTextArea textArea = new JTextArea();
            textArea.setPreferredSize(new Dimension(800, 25));
            textBoxToSendMessagePanel.add(textArea);
            JButton sendButton = new JButton("Send");
            sendButton.setPreferredSize(new Dimension(100, 25));
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(user, textArea.getText());
                }
            });
            textBoxToSendMessagePanel.add(sendButton);
        }
        JPanel conversationWrapper = new JPanel();
        messageWrapperBox.removeAll();
        userGettingMessagedLabel = new JLabel(user);
        messageWrapperBox.add(textBoxToSendMessagePanel, BorderLayout.SOUTH);
        messageWrapperBox.add(userGettingMessagedLabel, BorderLayout.NORTH);
        conversationWrapper.removeAll();
        conversationWrapper.setLayout(new BoxLayout(conversationWrapper, BoxLayout.Y_AXIS));
        int i = 0;
        if (!(messages.length == 0)) {
            for (String message : messages) {
                if (message.startsWith(user + " :")) {
                    conversationWrapper.add(new JLabel(message));
                } else {
                    JButton button = new JButton(message);
                    button.setPreferredSize(new Dimension(400, 25));
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                    int messageIndex = i;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            int option = JOptionPane.showConfirmDialog(null, "Would You Like to Delete This Message?", "Caution", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                            if (option == JOptionPane.YES_OPTION) {
                                deleteMessage(user, messageIndex);
                            }
                        }
                    });
                    conversationWrapper.add(button);
                }
                i++;
            }
        }


        JScrollPane conversationContentsPanel = new JScrollPane();
        conversationContentsPanel.setViewportView(conversationWrapper);
        conversationContentsPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conversationContentsPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageWrapperBox.add(conversationContentsPanel, BorderLayout.CENTER);

        rightPanel.add(messageWrapperBox);

        content.revalidate();
        content.repaint();

    }

    public static void bigWhiteBoxOtherUserView(String user, String bio, boolean isBlocked, boolean isFriend) {
        rightPanel.removeAll();
        JPanel otherUser = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel(user);
        usernameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 35));
        JLabel bioLabel = new JLabel(bio);
        usernameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 22));


        userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setOpaque(false);
        userPanel.add(usernameLabel);

        bioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bioPanel.setOpaque(false);
        bioPanel.add(bioLabel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        updateButtons(user, isBlocked, isFriend);


        otherUser.add(userPanel, BorderLayout.NORTH);
        otherUser.add(bioPanel, BorderLayout.CENTER);
        otherUser.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.add(otherUser);
        content.revalidate();
        content.repaint();
    }

    public static void updateButtons(String person, boolean isFriend, boolean isBlocked) {
        buttonPanel.removeAll();
        String blocker;
        String friendAdder;
        if (isBlocked) {
            blocker = "Unblock";
        } else {
            blocker = "Block";
        }
        if (isFriend) {
            friendAdder = "Remove Friend";
        } else {
            friendAdder = "Add Friend";
        }
        JButton blockButton = new JButton(blocker);
        JButton friendButton = new JButton(friendAdder);

        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isBlocked) {
                    unblockUser(person);
                } else {
                    blockUser(person);
                }
            }
        });

        friendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFriend) {
                    removeFriend(person);
                } else {
                    addFriend(person);
                }
            }
        });
        JButton messageButton = new JButton("Message");
        messageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMessageHistory(person);
            }
        });

        buttonPanel.add(messageButton);
        buttonPanel.add(friendButton);
        buttonPanel.add(blockButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    public static void bigWhiteBoxMyView(String user, String bio) {
        JPanel myUser = new JPanel(new BorderLayout());

        JLabel usernameLabel = new JLabel(user);
        usernameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 35));
        JLabel bioLabel = new JLabel(bio);
        usernameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 22));


        userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setOpaque(false);
        userPanel.add(usernameLabel);

        bioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bioPanel.setOpaque(false);
        bioPanel.removeAll();
        bioPanel.add(bioLabel);

        JButton editBioButton = new JButton("Edit Bio");
        editBioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newBio = JOptionPane.showInputDialog(null, "Please enter new bio:", bio);
                if (newBio != null && !newBio.trim().isEmpty()) {
                    setMyUserInfo(newBio);
                }
            }
        });

        bioPanel.add(editBioButton);

        myUser.add(userPanel, BorderLayout.NORTH);
        myUser.add(bioPanel, BorderLayout.CENTER);
        myUser.add(editBioButton, BorderLayout.SOUTH);
        rightPanel.removeAll();
        rightPanel.add(myUser);
        content.revalidate();
        content.repaint();
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("SINSTAGRAM");
            Toolkit tk = Toolkit.getDefaultToolkit();
            int xSize = ((int) tk.getScreenSize().getWidth());
            int ySize = ((int) (tk.getScreenSize().getHeight() * .95));
            frame.setSize(xSize, ySize);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            content = frame.getContentPane();
            content.setLayout(new BorderLayout());
            registerOrLogin(content);
            frame.setVisible(true);
        });
    }

    public static void getMessageUsernames() {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Send who I've talked to plz :)"});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void getFriendUsernames() {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Find my friends plz :)"});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void getBlockedUsernames() {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Find my haters >:("});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void getMyUser() {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Send my personal info over!"});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void setMyUserInfo(String info) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"update my bio", info});
                oos.flush();
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error requesting to update bio", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void getUser(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Send their personal info over>:)", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void getMessageHistory(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Send our conversation over plz :)", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void sendMessage(String usernameToSendTo, String message) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"I am sending a message.", usernameToSendTo, message});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void addFriend(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Add my friend please", username});
                oos.flush();
            }
        } catch (Exception ioe) {
            JOptionPane.showMessageDialog(null, "Error requesting to add friend", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void removeFriend(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"They are dead to me. Unfriend please :)", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void blockUser(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Block this b", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            return;
        }
    }

    public static void unblockUser(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"They chill now", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error when requesting to unblock user", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void searchUser(String username) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Find users with this string.", username});
                oos.flush();
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error searching users", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void deleteMessage(String username, int index) {
        try {
            synchronized (o) {
                oos.writeObject(new Object[]{"Lets remove this...", username, index});
                oos.flush();
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error removing mesage", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("localhost", 4500);
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Connecting to Server", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Client app = new Client();
            Thread t = new Thread(app);
            t.start();
        });
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object[] serverInput = (Object[]) ois.readObject();
                String command = (String) serverInput[0];
                switch (command) {
                    case "Register":
                        if (((String) serverInput[1]).contains("Successfully")) {
                            runAfterLogin(content, (String) serverInput[2], (String) serverInput[3]);
                            register.setVisible(false);
                            break;
                        }
                        register.setVisible(false);
                        registerOrLogin(content);
                        JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "Login":
                        if (((String) serverInput[1]).contains("Successfully")) {
                            runAfterLogin(content, (String) serverInput[2], (String) serverInput[3]); //TODO add user and bio
                            login.setVisible(false);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "ERROR", JOptionPane.ERROR_MESSAGE);
                            login.setVisible(false);
                            registerOrLogin(content);
                            break;
                        }
                    case "update yo messages":
                        if (((String) dropDown.getSelectedItem()).equals("Messages")) {
                            displayResults(leftScrollPanel, new ArrayList<String>(Arrays.asList((String[]) serverInput[2])));
                        }
                        if (!((boolean) serverInput[4]) || (userGettingMessagedLabel.isVisible() && userGettingMessagedLabel.getText().equals((String) serverInput[3]))) {
                            messageBox((String) serverInput[3], (String[]) serverInput[1], (boolean) serverInput[4]);
                        } else {
                            JOptionPane.showMessageDialog(null, "Message From " + (String) serverInput[3], "Message!", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Update User List":
                        String[] array = (String[]) serverInput[1];
                        displayResults(leftScrollPanel, new ArrayList<String>(Arrays.asList(array)));
                        break;
                    case "My User Info":
                        bigWhiteBoxMyView((String) serverInput[1], (String) serverInput[2]);
                        break;
                    case "Other User Info":
                        bigWhiteBoxOtherUserView((String) serverInput[1], (String) serverInput[2], (boolean) serverInput[3], (boolean) serverInput[4]);
                        break;
                    case "Update Friends":
                        if (((String) serverInput[1]).contains("User Added") || ((String) serverInput[1]).contains("Friend Removed")) {
                            if (((String) dropDown.getSelectedItem()).equals("Friends")) {
                                displayResults(leftScrollPanel, new ArrayList<>(Arrays.asList((String[]) serverInput[3])));
                            }
                            updateButtons((String) serverInput[2], (boolean) serverInput[4], (boolean) serverInput[5]);
                            JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "Update Blocked":
                        if (((String) serverInput[1]).contains("User Blocked") || ((String) serverInput[1]).contains("User Unblocked")) {
                            if (((String) dropDown.getSelectedItem()).equals("Blocked")) {
                                displayResults(leftScrollPanel, new ArrayList<>(Arrays.asList((String[]) serverInput[3])));
                            } else if (((String) dropDown.getSelectedItem()).contains("Friends")) {
                                displayResults(leftScrollPanel, new ArrayList<>(Arrays.asList((String[]) serverInput[6])));
                            }
                            updateButtons((String) serverInput[2], (boolean) serverInput[4], (boolean) serverInput[5]);
                            JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "Success", JOptionPane.INFORMATION_MESSAGE);
                            //TODO: Using arr, call method to update the friend username list on the left side
                            //TODO: Using arr, call method to update the user's page that had a friend status changed
                        } else {
                            JOptionPane.showMessageDialog(null, ((String) serverInput[1]), "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "Message Error":
                        JOptionPane.showMessageDialog(null, "Error: Illegal Character in Message", "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "Message too long":
                        JOptionPane.showMessageDialog(null, "Error: Message too long or message empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "Not Friend Error":
                        JOptionPane.showMessageDialog(null, "Error: You are Not Friends", "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "bio error":
                        JOptionPane.showMessageDialog(null, (String) serverInput[1], "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "Exit":
                        throw new Exception("Bye");
                    default:
                        JOptionPane.showMessageDialog(null, "Something went horribly, horribly wrong :(", "ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "A fatal error has occurred",
                    "Connection Closed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
