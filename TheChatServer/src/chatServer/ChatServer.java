package chatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable {
    /*................HANDLES THE SERVER LISTENING AND CONNECTION FUNCTIONALITIES.................*/
    /*------------------------------------------------------------------------------------------*/

    private static int serverPort;
    private static ServerSocket serverSocket;

    public static ArrayList<Socket> onlineClients;
    public static ArrayList<String> users;
    public static ArrayList<String> userMessages;

    public ChatServer() {
    }

    public static void setPort(int serverP) {

        serverPort = serverP;
        onlineClients = new ArrayList<>();
        users = new ArrayList<>();
        userMessages=new ArrayList<>();
    }

    public static ServerSocket getListenerSocket() throws IOException {

        serverSocket = new ServerSocket(serverPort);
        return serverSocket;
    }

    @Override
    public void run() {

        try {
            ServerSocket listener;
            Socket server;
            setPort(1234);
            listener = (ServerSocket) getListenerSocket();
            
            System.out.println("=========================================================================");
            System.out.println("SERVER IS RUNNING AND WAITING FOR CLIENT CONNECTIONS ................");
            System.out.println("==========================================================================");

            /*========================THIS ENABLES THE SERVER TO LISTEN CONTINOUSLY==============================================*/
            for (;;) {
                server = listener.accept();
                System.out.println("Client/Server Connection Established:");
                System.out.println("Client Host Name: "+server.getInetAddress().getHostName());
                System.out.println("Client IP Address: "+server.getInetAddress().getHostAddress()+": "+server.getPort());
                ChatServerHelper helper = new ChatServerHelper(server);
                Thread helperThread = new Thread(helper);
                helperThread.start();
            }
            

        } catch (IOException ex) {
            System.err.println("Server RunServer(): " + ex.getMessage());
        }

        try {
            getListenerSocket().close();

        } catch (IOException ex) {
            System.err.println("ChatServer run ServerSocket closing: " + ex.getMessage());
        }
    }

}