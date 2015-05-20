
package chatServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChatServerController implements ActionListener {

    public JFrame launchWindow;
    public JPanel launchPanel;
    public JLabel lblLaunchName1, lblLaunchName2, lblLaunchWelcome;
    public JButton btnStart, btnExit;

    public ChatServerController() {
    }

    public void displayServerLauncher() {

        launchWindow = new JFrame("Chat Server Controller");
        launchPanel = new JPanel();
        launchPanel.setLayout(null);
        launchPanel.setBackground(Color.LIGHT_GRAY);
        lblLaunchName1 = new JLabel("UltisofTech", SwingConstants.CENTER);
        lblLaunchName1.setFont(new Font("Verdana", Font.BOLD, 25));
        lblLaunchName1.setForeground(Color.BLUE);
        lblLaunchName1.setBounds(50, 5, 200, 20);
        lblLaunchName2 = new JLabel("Chat App", SwingConstants.CENTER);
        lblLaunchName2.setFont(new Font("Verdana", Font.PLAIN, 18));
        lblLaunchName2.setForeground(Color.BLACK);
        lblLaunchName2.setBounds(50, 30, 200, 20);
        lblLaunchWelcome = new JLabel("Welcome Window");
        lblLaunchWelcome.setFont(new Font("Monotype corsiva", Font.PLAIN, 18));
        lblLaunchWelcome.setForeground(Color.BLACK);
        lblLaunchWelcome.setBounds(100, 55, 150, 20);

        btnStart = new JButton("Start Chat Server");
        btnStart.setBounds(15, 80, 255, 40);
        btnStart.setFont(new Font("Verdana", 0, 24));
        btnStart.addActionListener(this);
        btnExit = new JButton("Stop Chat Server");
        btnExit.setBounds(15, 125, 255, 40);
        btnExit.setFont(new Font("Verdana", 0, 20));
        btnExit.setEnabled(false);
        btnExit.addActionListener(this);

        launchPanel.add(lblLaunchName1);
        launchPanel.add(lblLaunchName2);
        launchPanel.add(lblLaunchWelcome);
        launchPanel.add(btnExit);
        launchPanel.add(btnStart);

        launchWindow.add(launchPanel);
        launchWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        launchWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit this app?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == 0) {
                    System.exit(0);
                }
            }
        });

        launchWindow.setResizable(false);
        launchWindow.setLocationRelativeTo(null);
        launchWindow.setSize(300, 200);
        launchWindow.setVisible(true);
    }

    public static void main(String[] args) {
        /*====================================================================================================*/
        /*................ENTRY POINT TO THE CHAT SERVER AND CONTROLLER*/
        /*=====================================================================================================*/

        ChatServerController serverController = new ChatServerController();
        serverController.displayServerLauncher();
        /*=================================================================================*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnStart) {

            ChatServer chatServer = new ChatServer();
            Thread controller = new Thread(chatServer);
            controller.start();
            btnStart.setEnabled(false);
            btnExit.setEnabled(true);

        } else if (e.getSource() == btnExit) {

            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the Chat Server?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);

            if (response == 0) {

                try {
                    ChatServerLogic serverLogic = new ChatServerLogic();
                    serverLogic.serverShuttingDown();
                    
                    System.out.println("=============================================");
                    System.out.println("SERVER IS SHUTTING DOWN PLEASE ADMINISTRATOR.................");
                    System.out.println("==============================================");

                    btnStart.setEnabled(true);
                    System.exit(0);

                } catch (Exception ex) {
                    System.out.println("Main controller handler: " + ex.getMessage());
                }
            }

        }
    }
}
