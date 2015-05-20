
package chatServer;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import chatDatabase.DbConnection;

public class ChatServerLogic {

    DbConnection connection = new DbConnection();

    public ChatServerLogic() {
    }

    public void getAuthentication(String username, String password, DataOutputStream out, Socket server) {

        String response;
        boolean a = connection.authenticateMember(username, password);

        if (a == false) {
            try {
                response = ChatServerHelper.logfail;
                out.writeUTF(response);
                out.flush();

            } catch (IOException ex) {
                System.err.println("getAuthentication fail error: " + ex.getMessage());
            }
        } else {
            try {
                response = ChatServerHelper.logpass + ChatServerHelper.AON + username;
                out.writeUTF(response);
                out.flush();
                loginStatus(username);

                ChatServer.onlineClients.add(server);
                ChatServer.users.add(username);

            } catch (IOException ex) {
                System.err.println("getAuthentication pass error: " + ex.getMessage());
            }
        }
    }

    public void loginStatus(String username) {

        int numberOfOnlineUsers = ChatServer.onlineClients.size();
        DataOutputStream output;

        for (int counter = 0; counter < numberOfOnlineUsers; counter++) {

            try {
                Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String loginMessage = " is logged on!";
                String loginStatus = ChatServerHelper.loginState + ChatServerHelper.AON + username + ChatServerHelper.AON + loginMessage;
                output.writeUTF(loginStatus);
                output.flush();

            } catch (IOException ex) {
                System.err.println("Login status error: " + ex.getMessage());
            }

        }
    }

    public void refreshUsers() {

        int numberOfOnlineUsers = ChatServer.onlineClients.size();
        DataOutputStream output;

        for (int counter = 0; counter < numberOfOnlineUsers; counter++) {

            try {
                Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                String user = (String) ChatServer.users.get(counter);
                output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                String users = "";
                for (int t = 0; t < numberOfOnlineUsers; t++) {
                    users = users + ChatServerHelper.AON + (ChatServer.users.get(t)).toUpperCase();
                }

                String userList = users.substring(ChatServerHelper.AON.length());
                String usersList = ChatServerHelper.refreshUserList + ChatServerHelper.AON + "Number of Online users: " + numberOfOnlineUsers + ChatServerHelper.AON + userList;
                output.writeUTF(usersList);
                output.flush();

            } catch (IOException ex) {
                System.err.println("Error in server Users refreshing: " + ex.getMessage());
            }
        }
    }
    
    public void refreshAtLogin(DataOutputStream out) {
        
        try {
            System.out.println("User needs refreshing please at login please...................");
            int numberOfOnlineUsers = ChatServer.onlineClients.size();
            
            DataOutputStream output;
            String message=ChatServerHelper.refreshAtLogin;
            
            for (int counter = 0; counter < numberOfOnlineUsers; counter++) {
                
                try {
                    Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                    String user = (String) ChatServer.users.get(counter);
                    output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    
                    String users = "";
                    for (int t = 0; t < numberOfOnlineUsers; t++) {
                        users = users + ChatServerHelper.AON + (ChatServer.users.get(t)).toUpperCase();
                    }
                    
                    String userList = users.substring(ChatServerHelper.AON.length());
                    String usersList = ChatServerHelper.refreshUserList + ChatServerHelper.AON + "Number of Online users: " + numberOfOnlineUsers + ChatServerHelper.AON + userList;
                    output.writeUTF(usersList);
                    output.flush();
                    
                } catch (IOException ex) {
                    System.err.println("Error in server Users refreshing: " + ex.getMessage());
                }
            }
            if(!(ChatServer.userMessages.isEmpty())){
            for (String userMessage : ChatServer.userMessages) {
                
                String username=capitalizeString(userMessage.substring(0,userMessage.indexOf(ChatServerHelper.AON)));
                String mess=userMessage.substring(userMessage.indexOf(ChatServerHelper.AON)+ChatServerHelper.AON.length(), userMessage.lastIndexOf(ChatServerHelper.AON));
                String time=userMessage.substring(userMessage.lastIndexOf(ChatServerHelper.AON)+ChatServerHelper.AON.length());
                String mess1=username+":>"+mess+"[ sent:"+time+"]";
                message=message+ChatServerHelper.AON+mess1;
            }
            out.writeUTF(message);
            out.flush();
            }
        } catch (IOException ex) {
            System.err.println("Server refreshAtLogin: "+ex.getMessage());
        }
    }

    public void manageChats(ArrayList<String> receivedChatMessageFields) {

        int numberOfOnlineUsers = ChatServer.onlineClients.size();
        String messageToUsers = receivedChatMessageFields.get(0);
        String senderUsername = receivedChatMessageFields.get(1);
        String timeSent=receivedChatMessageFields.get(2);
        
        ChatServer.userMessages.add(senderUsername+ChatServerHelper.AON+messageToUsers+ChatServerHelper.AON+timeSent);
        
        DataOutputStream output;

        for (int counter = 0; counter < numberOfOnlineUsers; counter++) {
            try {
                Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                String usersMessage = ChatServerHelper.chatMessage + ChatServerHelper.AON + senderUsername + ChatServerHelper.AON + messageToUsers+ChatServerHelper.AON+timeSent;

                output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                output.writeUTF(usersMessage);
                output.flush();

            } catch (IOException ex) {
                System.err.println("IO error during sending chat to onlineClients: " + ex.getMessage());
            }
        }
    }

    public void logoutHandler(String username, DataOutputStream out) {

        try {

            int userIndex = ChatServer.users.indexOf(username);

            ChatServer.onlineClients.remove(userIndex);
            ChatServer.users.remove(username);

            int numberOfOnlineUsers = ChatServer.onlineClients.size();
            DataOutputStream output;

            for (int counter = 0; counter < numberOfOnlineUsers; counter++) {

                try {
                    Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                    output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    String logoutMessage = " is logged out!";
                    String logoutStatus = ChatServerHelper.logoutState + ChatServerHelper.AON + username + ChatServerHelper.AON + logoutMessage;
                    output.writeUTF(logoutStatus);
                    output.flush();

                } catch (IOException ex) {
                    System.err.println("Error during logout processing: " + ex.getMessage());
                }

            }

            refreshUsers();

            out.writeUTF(ChatServerHelper.logout);
            out.flush();

        } catch (IOException ex) {
            System.err.println("Logout message to client error: " + ex.getMessage());
        }
    }

    public void registerUser(String regUsername, String regPassword, DataOutputStream out) {

        boolean confirm = connection.registerUser(regUsername, regPassword);

        if (confirm == true) {
            try {
                out.writeUTF(ChatServerHelper.regSuccess + ChatServerHelper.AON + regUsername);
                out.flush();

            } catch (IOException ex) {
                System.err.println("Register user at Server logic: " + ex.getMessage());
            }
        } else {
            try {
                out.writeUTF(ChatServerHelper.regFail + ChatServerHelper.AON + regUsername);
                out.flush();

            } catch (IOException ex) {
                System.err.println("Register user at Server logic: " + ex.getMessage());
            }
        }
    }

    public void serverShuttingDown() {

        int numberOfOnlineUsers = ChatServer.onlineClients.size();
        DataOutputStream output;

        for (int counter = 0; counter < numberOfOnlineUsers; counter++) {

            try {
                Socket socket = (Socket) ChatServer.onlineClients.get(counter);
                output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String shutDownMessage = ChatServerHelper.shuttingDown;
                output.writeUTF(shutDownMessage);
                output.flush();
            } catch (IOException ex) {
                System.err.println("Login status error: " + ex.getMessage());
            }

        }
    }
    
    public String capitalizeString(String user) {

        char[] usernameCharacters = new char[user.length()];
        String[] usernameCharacterString = new String[user.length()];
        String usernameCapitalize = "";

        for (int c = 0; c < user.length(); c++) {
            usernameCharacters[c] = user.charAt(c);
        }

        for (int c = 0; c < user.length(); c++) {
            usernameCharacterString[c] = String.valueOf(usernameCharacters[c]);
            if (c == 0) {
                usernameCharacterString[c] = usernameCharacterString[c].toUpperCase();
            }
            usernameCapitalize += usernameCharacterString[c];
        }
        return usernameCapitalize;
    }
}