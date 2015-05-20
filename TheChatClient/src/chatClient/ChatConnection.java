
package chatClient;
import java.net.*;
import java.io.*;

public class ChatConnection {
    
    public  Socket socket;
    public  DataInputStream in;
    public  DataOutputStream out;
    
    public ChatConnection() {}
    
    public  void openConnection() {
        
        String hostName="localhost";
        int serverPort=1234;
    
        try {
            
            InetAddress address=InetAddress.getByName(hostName);
            socket=new Socket(address, serverPort);
            
            System.out.println("=======================================================================");
            System.out.println("CLIENT SUCCESSFULLY CONNECTED TO THE CHAT SERVER............");
            System.out.println("=======================================================================");
            
            in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            
        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException in openConnection(): "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException in openConnection(): "+ex.getMessage());
        }
    }
    
    
}
