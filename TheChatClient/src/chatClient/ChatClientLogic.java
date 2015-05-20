package chatClient;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class ChatClientLogic {

    public ChatClientLogic() {
    }

    public void loginSuccess(String title, DataOutputStream output, ChatDisplay chatDisplay) {
        try {

            JOptionPane.showMessageDialog(null, "Thank you " + title.toUpperCase() + " for logging in", "Success", JOptionPane.INFORMATION_MESSAGE);
            //output.writeUTF(ChatDisplay.refreshUserList);
            output.writeUTF(ChatDisplay.refreshAtLogin);
            output.flush();
            chatDisplay.displayChatWindow();
            chatDisplay.mainFrame.setTitle(title.toUpperCase() + "'s Chat Screen");

        } catch (IOException ex) {
            System.out.println("loginSuccess(): " + ex.getMessage());
        }
    }

    public void loginFailure(ChatDisplay chatDisplay) {

        int i = JOptionPane.showConfirmDialog(null, "Incorrect login details!.Do you wish to try again?", "Failure", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (i == 0) {
            chatDisplay.displayLoginWindow();
        } else {

            int t = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the system?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (t == 0) {
                System.exit(0);
            } else {
                chatDisplay.displayLoginWindow();
            }
        }
    }

    public void loginStatus(String receivedMessage, ChatDisplay chatDisplay) {

        String loginStatusMessageBody = receivedMessage.substring(ChatDisplay.loginState.length() + ChatDisplay.AON.length());
        int sepIndex = loginStatusMessageBody.indexOf(ChatDisplay.AON);
        String user = loginStatusMessageBody.substring(0, sepIndex);
        String userCapitalize = capitalizeString(user);
        String loginStatusMessage = loginStatusMessageBody.substring(sepIndex + ChatDisplay.AON.length());
        chatDisplay.txtLogStatus.append(userCapitalize + loginStatusMessage + "\n");
        chatDisplay.txtAvailableUsers.append(userCapitalize + "\n");
    }

    public void refreshUsers(String receivedMessage, ChatDisplay chatDisplay) {

        String refreshMessageBody = receivedMessage.substring(ChatDisplay.refreshUserList.length() + ChatDisplay.AON.length());
        chatDisplay.txtAvailableUsers.setText(refreshMessageBody);
    }
    
    public void refreshAtlogin(String receivedMessage,ChatDisplay chatDisplay) {

        System.out.println("Uploads Previous messages.......................");
        System.out.println("-.-.-.-.-.-.-.-.-.-.-.-.-.-");
        
        String message=receivedMessage.substring(ChatDisplay.refreshAtLogin.length()+ChatDisplay.AON.length());
        chatDisplay.txtMessages.setText(message+"\n");
    }

    public void controlChats(String receivedChatMessage, ChatDisplay chatDisplay) throws ParseException {

        String displayMessage = receivedChatMessage.substring(ChatDisplay.chatDisplayMessage.length() + ChatDisplay.AON.length());
        int index = displayMessage.indexOf(ChatDisplay.AON);
        int indexL=displayMessage.lastIndexOf(ChatDisplay.AON);
        String usernameDisplayed = displayMessage.substring(0, index);
        String usernameDisplayedCapitalize = capitalizeString(usernameDisplayed);
        String messageDisplayed = displayMessage.substring(index + ChatDisplay.AON.length(),indexL);
        String timeSent=displayMessage.substring(indexL+ChatDisplay.AON.length());
        chatDisplay.txtMessages.append(usernameDisplayedCapitalize + ":> " + messageDisplayed+" [sent: "+timeSent+"]"+ "\n");

    }

    public void logoutStatus(String receivedMessage, ChatDisplay chatDisplay) {

        int indexInitial = receivedMessage.indexOf(ChatDisplay.AON);
        int indexFinal = receivedMessage.lastIndexOf(ChatDisplay.AON);
        String user = receivedMessage.substring(ChatDisplay.logoutState.length() + ChatDisplay.AON.length(), indexFinal);
        String logoutStatusMessage = receivedMessage.substring(indexFinal + ChatDisplay.AON.length());
        String usernameCapitalize = capitalizeString(user);
        chatDisplay.txtLogStatus.append(usernameCapitalize + logoutStatusMessage + "\n");
    }

    public void logoutListener(DataOutputStream output, ChatDisplay chatDisplay) {

        try {

            int i = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Logout Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == 1) {
                return;
            }

            String usertext = chatDisplay.mainFrame.getTitle();
            int ind = usertext.indexOf("'");
            String user = usertext.substring(0, ind);

            String logoutMessage = ChatDisplay.logout + ChatDisplay.AON + user.toLowerCase();

            output.writeUTF(logoutMessage);
            output.flush();

            chatDisplay.mainFrame.dispose();
            chatDisplay.launchWindow.setVisible(true);

        } catch (IOException ex) {
            System.out.println("Error in logoutListener: " + ex.getMessage());
        }
    }

    public void registrationSuccess(String username, ChatDisplay chatDisplay) {

        JOptionPane.showMessageDialog(null, "You are successfully registered as " + username, "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        chatDisplay.displayLoginWindow();
        chatDisplay.registerWindow.dispose();
    }

    public void registrationFailure(String username) {
        JOptionPane.showMessageDialog(null, "The username, " + username.toUpperCase() + " already exists.Please provide a unique username", "Registration prompt", JOptionPane.PLAIN_MESSAGE);
    }

    public void unexpectedShutDown(DataOutputStream output, ChatDisplay chatDisplay1) {

        try {

            JOptionPane.showMessageDialog(null, "Server encountered an error and is shutting down", "Server error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            output.writeUTF(ChatDisplay.shuttingDown);
            output.flush();

        } catch (IOException ex) {
            System.out.println("Unexpected shutDown: " + ex.getMessage());
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
