
package chatServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerHelper implements Runnable {

    private final Socket server;
    public DataInputStream in;
    public DataOutputStream out;

    final static String auth = "AUTHENTICATE";
    final static String logfail = "LOGFAIL";
    final static String logpass = "LOGPASS";
    final static String logout = "LOGOUT";
    final static String shuttingDown = "SHUTTINGDOWN";
    final static String AON = System.getProperty("line.separator");
    final static String chatMessage = "CHATMESSAGE";
    final static String loginState = "LOGINSTATE";
    final static String logoutState = "LOGOUTSTATE";
    final static String refreshUserList = "REFRESH";
    final static String refreshAtLogin="PREFRESHATLOGIN";
    final static String regUser = "REGUSER";
    final static String regSuccess = "REGSUCCESS";
    final static String regFail = "REGFAIL";

    public ChatServerLogic serverLogic;

    public ChatServerHelper(Socket s) {
        this.server = s;
    }

    @Override
    public void run() {
        /*==================================THE MMAIN ENTRY POINT FOR CLIENT COMMUNICATION ON THE SERVER SIDE============================*/
        /*--------------------------------------------------------------------------------------------------------------------*/
        try {

            in = new DataInputStream(new BufferedInputStream(server.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
            serverLogic = new ChatServerLogic();

            /*..............................................................................................................................*/
//                                          MAIN LOOP            
/*...............................................................................................................................*/
            System.out.println("Your are now at the server's client thread=========================================================");

            while (true) {     // The main while body- Where sending and receiving of messages occur.

                ArrayList<String> receivedMessageFields = new ArrayList<>();
                String receivedMessage = "";

                int distinguish = 0;//To categorise received/sent messages

                String receivedRawMessage = in.readUTF();    // RECEIVED MESSAGE FROM CLIENT

                if (receivedRawMessage.startsWith(auth)) {
                    receivedMessage = receivedRawMessage.substring(auth.length() + AON.length());
                    distinguish = 1;
                } else if (receivedRawMessage.startsWith(logout)) {
                    receivedMessage = receivedRawMessage.substring(logout.length() + AON.length());
                    distinguish = 6;
                } else if (receivedRawMessage.startsWith(refreshUserList)) {
                    receivedMessage = receivedRawMessage;
                    distinguish = 7;
                } else if (receivedRawMessage.startsWith(chatMessage)) {
                    receivedMessage = receivedRawMessage.substring(chatMessage.length() + AON.length());
                    distinguish = 8;
                } else if (receivedRawMessage.startsWith("backlogin")) {
                    distinguish = 9;
                } else if (receivedRawMessage.startsWith(regUser)) {
                    receivedMessage = receivedRawMessage.substring(regUser.length() + AON.length());
                    distinguish = 10;
                } else if (receivedRawMessage.startsWith(shuttingDown)) {
                    distinguish = 13;
                } else if(receivedRawMessage.startsWith(refreshAtLogin)) {distinguish=14;}

                int separatorIndex = receivedMessage.indexOf(AON);
                String receivedMessage1 = receivedMessage;

                while (receivedMessage1.contains(AON)) {
                    String receivedMessageField = receivedMessage1.substring(0, separatorIndex);
                    receivedMessageFields.add(receivedMessageField); //Puts various message fields in an array
                    receivedMessage1 = receivedMessage1.substring(separatorIndex + AON.length());
                    separatorIndex = receivedMessage1.indexOf(AON);

                }

                receivedMessageFields.add(receivedMessage1);

                switch (distinguish) {

                    case 1:  // Handles authentication tasks
                        serverLogic.getAuthentication(receivedMessageFields.get(0), receivedMessageFields.get(1), out, server);
                        break;

                    case 6:
                        serverLogic.logoutHandler(receivedMessage, out);
                        break;

                    case 7:
                        serverLogic.refreshUsers();
                        break;

                    case 8:
                        serverLogic.manageChats(receivedMessageFields);
                        break;

                    case 9:
                        out.writeUTF("backlogin");
                        out.flush();
                        break;

                    case 10:
                        serverLogic.registerUser(receivedMessageFields.get(0), receivedMessageFields.get(1), out);
                        break;

                    case 13:
                        System.out.println("Unexpected server shutdown");
                        break;
                        
                    case 14:
                        serverLogic.refreshAtLogin(out);
                }

                if (distinguish == 6 || distinguish == 9 || distinguish == 13) {
                    break;
                }
            } // End of the main while loop body
/*..................................................................................................................................*/
            /*...................................................................................................................................*/
            System.out.println("Client connection terminating.......");
            server.close();
            
             if(ChatServer.onlineClients.isEmpty()) {
                 System.out.println("===========================================================================================================");
                 System.out.println("SERVER IS NOW IDLE: NO CLIENT CONNECTION ESTABLISHED");
                 System.out.println("============================================================================================================");
             }

        } catch (IOException ex) {
            System.out.println("Error in ChatServerHelper: " + ex.getMessage());
        }
        /*---------------------------------------------------------------------------------------------------------------------------------------------------------*/
    }

}
