package chatClient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatLauncher implements ActionListener {

    public JFrame launchWindow;
    public JPanel launchPanel;
    public JLabel lblLaunchName1, lblLaunchName2, lblLaunchWelcome;
    public JButton btnRegister, btnStart, btnExit;
    public ChatConnection chatConnection;

    public ChatLauncher() {
    }

    public void displayLauncher() {

        launchWindow = new JFrame("Chat Client Launcher");
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
        btnRegister = new JButton("Register");
        btnRegister.setBounds(10, 100, 100, 30);

        btnStart = new JButton("Start Chat Session");
        btnStart.setBounds(15, 80, 255, 40);
        btnStart.setFont(new Font("Verdana", 0, 24));
        btnStart.addActionListener(this);
        btnExit = new JButton("Terminate Application");
        btnExit.setBounds(15, 125, 255, 40);
        btnExit.setFont(new Font("Verdana", 0, 20));
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

        ChatLauncher chatLauncher = new ChatLauncher();
        chatLauncher.displayLauncher();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnStart) {
            chatConnection = new ChatConnection();
            chatConnection.openConnection();
            ChatDisplay chatDisplay = new ChatDisplay();
            chatDisplay.setChatDisplay(chatConnection.socket, chatConnection.in, chatConnection.out);
            Thread t = new Thread(chatDisplay);
            t.start();
            launchWindow.dispose();

        } else if (e.getSource() == btnExit) {

            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit this app?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (response == 0) {
                System.exit(0);
            }
        }
    }
}
