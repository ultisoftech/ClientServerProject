package chatClient;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ChatDisplay implements Runnable, ActionListener {
    /*..........Launch window............................................*/

    public JFrame launchWindow;
    public JPanel launchPanel;
    public JLabel lblLaunchName1, lblLaunchName2, lblLaunchWelcome;
    public JButton btnStart, btnExit;

    public JFrame registerWindow;
    public JPanel registerPanel;
    public JLabel lblRegisterName1, lblRegisterName2, lblRegisterWelcome, lblRegUsername, lblRegPassword, lblRegPassword1;
    public JTextField txtRegUsername;
    public JPasswordField txtRegPassword, txtRegPassword1;
    public JButton btnSubmit;

    JFrame loginWindow;
    JPanel panel;
    JLabel lblAppName1, lblAppName2, lblWelcome, lblUsername, lblPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnRegister;

    public JFrame mainFrame;
    public JPanel leftPanel, rightPanel, buttonPanel;
    public JScrollPane pane1, pane2, pane3;
    public JTextArea txtMessages, txtAvailableUsers, txtLogStatus;
    public JLabel lblCompany, lblAppName;
    public JButton btnSend, btnLogout;
    public JTextField txtCompose;
    public GridBagConstraints constraints;

    public DataInputStream input;
    public DataOutputStream output;
    public Socket socket;
    public ChatClientLogic clientLogic;

    /*...........Message format/Marker strings............................*/
    final static String auth = "AUTHENTICATE";
    final static String logfail = "LOGFAIL";
    final static String logpass = "LOGPASS";
    final static String logout = "LOGOUT";
    final static String shuttingDown = "SHUTTINGDOWN";
    final static String AON = System.getProperty("line.separator");
    final static String chatDisplayMessage = "CHATMESSAGE";
    final static String loginState = "LOGINSTATE";
    final static String logoutState = "LOGOUTSTATE";
    final static String refreshUserList = "REFRESH";
    final static String refreshAtLogin="PREFRESHATLOGIN";
    final static String regUser = "REGUSER";
    final static String regSuccess = "REGSUCCESS";
    final static String regFail = "REGFAIL";
    /*..............................................................................*/
    /*...............................................................................*/

    public ChatDisplay() {
    }

    public void setChatDisplay(Socket s, DataInputStream in, DataOutputStream out) {
        socket = s;
        input = in;
        output = out;
        clientLogic = new ChatClientLogic();
    }

    public void displayLauncher() {

        launchWindow = new JFrame("Chat Launcher");
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

    public void displayRegisterWindow() {

        registerWindow = new JFrame("Chat Register");
        registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBackground(Color.LIGHT_GRAY);
        lblRegisterName1 = new JLabel("UltisofTech", SwingConstants.CENTER);
        lblRegisterName1.setFont(new Font("Verdana", Font.BOLD, 25));
        lblRegisterName1.setForeground(Color.BLUE);
        lblRegisterName1.setBounds(50, 5, 200, 20);
        lblRegisterName2 = new JLabel("Chat App", SwingConstants.CENTER);
        lblRegisterName2.setFont(new Font("Verdana", Font.PLAIN, 18));
        lblRegisterName2.setForeground(Color.BLACK);
        lblRegisterName2.setBounds(50, 30, 200, 20);
        lblRegisterWelcome = new JLabel("Register Window");
        lblRegisterWelcome.setFont(new Font("Monotype corsiva", Font.PLAIN, 18));
        lblRegisterWelcome.setForeground(Color.BLACK);
        lblRegisterWelcome.setBounds(100, 55, 150, 20);
        lblRegUsername = new JLabel("Username: ");
        lblRegUsername.setFont(new Font("Verdana", 0, 14));
        lblRegUsername.setBounds(15, 80, 85, 25);
        lblRegPassword = new JLabel("Password: ");
        lblRegPassword.setFont(new Font("Verdana", 0, 14));
        lblRegPassword.setBounds(15, 110, 80, 25);
        lblRegPassword1 = new JLabel("Confirm: ");
        lblRegPassword1.setFont(new Font("Verdana", 0, 14));
        lblRegPassword1.setBounds(15, 140, 80, 25);
        txtRegUsername = new JTextField();
        txtRegUsername.setFont(new Font("Verdana", 0, 18));
        txtRegUsername.setBounds(110, 80, 100, 25);
        txtRegPassword = new JPasswordField();
        txtRegPassword.setFont(new Font("Verdana", 0, 18));
        txtRegPassword.setBounds(110, 110, 100, 25);
        txtRegPassword1 = new JPasswordField();
        txtRegPassword1.setFont(new Font("Verdana", 0, 18));
        txtRegPassword1.setBounds(110, 140, 100, 25);
        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(215, 110, 75, 40);
        btnSubmit.addActionListener(this);

        registerPanel.add(lblRegisterName1);
        registerPanel.add(lblRegisterName2);
        registerPanel.add(lblRegisterWelcome);
        registerPanel.add(lblRegUsername);
        registerPanel.add(txtRegUsername);
        registerPanel.add(lblRegPassword);
        registerPanel.add(txtRegPassword);
        registerPanel.add(lblRegPassword1);
        registerPanel.add(txtRegPassword1);
        registerPanel.add(btnSubmit);

        registerWindow.add(registerPanel);
        registerWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        registerWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel registration?", "Registration Abort Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == 0) {
                    displayLoginWindow();
                    registerWindow.dispose();
                }
            }
        });
        registerWindow.setResizable(false);
        registerWindow.setLocationRelativeTo(null);
        registerWindow.setSize(300, 200);
        registerWindow.setVisible(true);
    }

    public void displayLoginWindow() {

        loginWindow = new JFrame("Chat Login");
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.LIGHT_GRAY);
        lblAppName1 = new JLabel("UltisofTech", SwingConstants.CENTER);
        lblAppName1.setFont(new Font("Verdana", Font.BOLD, 25));
        lblAppName1.setForeground(Color.BLUE);
        lblAppName1.setBounds(100, 5, 200, 20);
        lblAppName2 = new JLabel("Chat App", SwingConstants.CENTER);
        lblAppName2.setFont(new Font("Verdana", Font.PLAIN, 18));
        lblAppName2.setForeground(Color.BLACK);
        lblAppName2.setBounds(100, 30, 200, 20);
        lblWelcome = new JLabel("Login");
        lblWelcome.setFont(new Font("Monotype corsiva", Font.PLAIN, 26));
        lblWelcome.setForeground(Color.BLACK);
        lblWelcome.setBounds(170, 55, 150, 25);
        lblUsername = new JLabel("Username: ");
        lblUsername.setBounds(50, 90, 100, 20);
        lblUsername.setFont(new Font("Impact", Font.PLAIN, 20));
        lblPassword = new JLabel("Password: ");
        lblPassword.setBounds(55, 135, 100, 20);
        lblPassword.setFont(new Font("Impact", Font.PLAIN, 20));
        txtUsername = new JTextField();
        txtUsername.setBounds(160, 85, 150, 30);
        txtUsername.setFont(new Font("Verdana", Font.PLAIN, 20));
        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 130, 150, 30);
        txtPassword.setFont(new Font("Verdana", Font.PLAIN, 20));
        btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 170, 100, 40);
        btnLogin.setFont(new Font("Cooper black", Font.PLAIN, 20));
        btnLogin.addActionListener(this);
        btnRegister = new JButton("Register");
        btnRegister.setBounds(170, 170, 140, 40);
        btnRegister.setFont(new Font("Cooper black", Font.PLAIN, 20));
        btnRegister.addActionListener(this);

        panel.add(lblAppName1);
        panel.add(lblAppName2);
        panel.add(lblWelcome);
        panel.add(lblUsername);
        panel.add(lblPassword);
        panel.add(txtUsername);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);

        loginWindow.add(panel);
        loginWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loginWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginWindowOut();
            }
        });

        ImageIcon logo = new ImageIcon("Images/logo.png");
        loginWindow.setIconImage(logo.getImage());
        loginWindow.setSize(400, 250);
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setResizable(false);
        loginWindow.setVisible(true);
    }

    public void displayChatWindow() {

        mainFrame = new JFrame("Layout Demostration");
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(100, 100));
        leftPanel.setBackground(Color.GRAY);
        leftPanel.setBorder(new TitledBorder(new LineBorder(Color.yellow, 2), "Chat Messages", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monotype corsiva", 0, 20), Color.BLUE));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        buttonPanel = new JPanel(new BorderLayout(20, 20));
        constraints = new GridBagConstraints();

        txtMessages = new JTextArea();
        txtMessages.setFont(new Font("Verdana", 0, 18));
        txtMessages.setLineWrap(true);
        txtMessages.setWrapStyleWord(true);
        txtMessages.setBounds(20, 20, 250, 300);
        txtMessages.setMargin(new Insets(5, 20, 5, 20));
        txtMessages.setEditable(false);

        txtCompose = new JTextField("Type message here", 8);
        txtCompose.setPreferredSize(new Dimension(130, 40));
        txtCompose.setFont(new Font("Arial", 0, 20));
        txtCompose.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                txtCompose.setFocusable(true);

                if (txtCompose.getText().equals("Type message here")) {
                    txtCompose.setText("");
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                if (txtCompose.getText().trim().length() <= 0) {
                    txtCompose.setText("Type message here");
                    txtCompose.setFocusable(false);
                }
            }
        });

        txtCompose.addActionListener(this);
        txtAvailableUsers = new JTextArea(1, 4);
        txtAvailableUsers.setFont(new Font("Verdana", 0, 18));
        txtAvailableUsers.setForeground(Color.GREEN);
        txtAvailableUsers.setMargin(new Insets(2, 5, 2, 0));
        txtAvailableUsers.setEditable(false);

        txtLogStatus = new JTextArea(1, 4);
        txtLogStatus.setFont(new Font("Verdana", 0, 18));
        txtLogStatus.setForeground(Color.GREEN);
        txtLogStatus.setMargin(new Insets(2, 5, 2, 0));
        txtLogStatus.setEditable(false);

        lblCompany = new JLabel("UltisofTech");
        lblCompany.setHorizontalTextPosition(SwingConstants.CENTER);
        lblCompany.setHorizontalAlignment(SwingConstants.CENTER);
        lblCompany.setFont(new Font("Monotype corsiva", 1, 26));
        lblCompany.setForeground(Color.BLUE);
        lblCompany.setMaximumSize(new Dimension(1000, 100));
        lblAppName = new JLabel("Chat App");
        lblAppName.setHorizontalTextPosition(SwingConstants.CENTER);
        lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
        lblAppName.setFont(new Font("Monotype corsiva", 1, 26));
        lblAppName.setForeground(Color.BLUE);

        btnSend = new JButton("Send");
        btnSend.setPreferredSize(new Dimension(100, 30));
        btnSend.addActionListener(this);
        btnLogout = new JButton("Log Out");
        btnLogout.setPreferredSize(new Dimension(100, 30));
        btnLogout.addActionListener(this);

        pane1 = new JScrollPane(txtMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane2 = new JScrollPane(txtAvailableUsers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane2.setBorder(new TitledBorder(new LineBorder(Color.yellow, 4), "Available Online users", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monotype corsiva", 1, 15), Color.GREEN));
        pane3 = new JScrollPane(txtLogStatus, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane3.setBorder(new TitledBorder(new LineBorder(Color.yellow, 4), "Logging Status", TitledBorder.CENTER, TitledBorder.TOP, new Font("Monotype corsiva", 1, 15), Color.GREEN));

        leftPanel.add(pane1);
        GridLayout layout = new GridLayout(1, 2);

        buttonPanel.add(btnSend, BorderLayout.WEST);
        buttonPanel.add(btnLogout, BorderLayout.EAST);

        rightPanel.add(lblCompany, setConstraints(0, 0, 1, 2, 2, 0, 0));
        rightPanel.add(lblAppName, setConstraints(0, 1, 1, 2, 2, 0, 0));
        rightPanel.add(pane2, setUsersConstraints(0, 2, 4, 2, 1, 1, 1));
        rightPanel.add(pane3, setUsersConstraints(0, 6, 4, 2, 1, 1, 1));
        rightPanel.add(txtCompose, setConstraints(0, 10, 1, 2, 2, 0, 0));
        rightPanel.add(buttonPanel, setConstraints(1, 12, 1, 1, 2, 0, 0));

        mainFrame.setLayout(layout);
        mainFrame.add(leftPanel);
        mainFrame.add(rightPanel);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logoutListener();
            }
        });

        mainFrame.setSize(500, 300);
        mainFrame.setMinimumSize(new Dimension(600, 300));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public GridBagConstraints setConstraints(int column, int row, int rows, int cols, int fl, int wtx, int wty) {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridheight = rows;
        constraints.gridwidth = cols;
        constraints.fill = fl;
        constraints.weightx = wtx;
        constraints.weighty = wty;
        return constraints;
    }

    public GridBagConstraints setUsersConstraints(int column, int row, int rows, int cols, int fl, int wtx, int wty) {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridheight = rows;
        constraints.gridwidth = cols;
        constraints.fill = fl;
        constraints.weightx = wtx;
        constraints.weighty = wty;
        constraints.insets = new Insets(0, 10, 0, 0);
        return constraints;
    }

    @Override
    public void run() {

        /*.......................MAIN CLIENT CHAT APPLICATION BODY................................*/
        /*.........................................................................................*/
        displayLoginWindow();
        /*--------------------------------------------------------------------------------------------------------------------------------------------*/
        try {
            /*.................................................................................................*/
 //                           MAIN LOOP          
/*..................................................................................................*/

            while (true) {   //THE MAIN WHILE LOOP OF THE PROGRAM FOR SENDING AND RECEIVING MESSAGES

                int distinguish = 0;

                String receivedMessage = input.readUTF();
                /*==================================================================================================*/
                if (receivedMessage.startsWith(logpass)) {
                    distinguish = 2;
                } else if (receivedMessage.startsWith(logfail)) {
                    distinguish = 3;
                } else if (receivedMessage.startsWith(loginState)) {
                    distinguish = 4;
                } else if (receivedMessage.startsWith(logoutState)) {
                    distinguish = 5;
                } else if (receivedMessage.startsWith(logout)) {
                    distinguish = 6;
                } else if (receivedMessage.startsWith(refreshUserList)) {
                    distinguish = 7;
                } else if (receivedMessage.startsWith(chatDisplayMessage)) {
                    distinguish = 8;
                } else if (receivedMessage.startsWith("backlogin")) {
                    distinguish = 9;
                } else if (receivedMessage.startsWith(regSuccess)) {
                    distinguish = 11;
                } else if (receivedMessage.startsWith(regFail)) {
                    distinguish = 12;
                } else if (receivedMessage.startsWith(shuttingDown)) {
                    distinguish = 13;
                } else if(receivedMessage.startsWith(refreshAtLogin)) {distinguish=14;}
                /*======================================================================================================*/

                switch (distinguish) {

                    case 2:
                        String title = receivedMessage.substring(logpass.length() + AON.length());
                        clientLogic.loginSuccess(title, output, this);
                        break;

                    case 3:
                        clientLogic.loginFailure(this);
                        break;

                    case 4:
                        clientLogic.loginStatus(receivedMessage, this);
                        break;

                    case 5:
                        clientLogic.logoutStatus(receivedMessage, this);
                        break;

                    case 6:
                        break;

                    case 7:
                        clientLogic.refreshUsers(receivedMessage, this);
                        break;

                    case 8:
                        clientLogic.controlChats(receivedMessage, this);
                        break;

                    case 9:
                        break;

                    case 11:
                        String regUsersuccess = receivedMessage.substring(regSuccess.length() + AON.length());
                        clientLogic.registrationSuccess(regUsersuccess, this);
                        break;

                    case 12:
                        String regUserfail = receivedMessage.substring(regFail.length() + AON.length());
                        clientLogic.registrationFailure(regUserfail);
                        break;

                    case 13:
                        clientLogic.unexpectedShutDown(output, this);
                        break;
                        
                    case 14:
                        clientLogic.refreshAtlogin(receivedMessage,this);
                        break;
                }

                if (distinguish == 6 || distinguish == 9 || distinguish == 13) {
                    break;
                }

            } //END OF MAIN WHILE LOOP

            /*.......................................................................................................................*/
            /*........................................................................................................................*/
            System.out.println("User has logged out of the system.......................");
            socket.close();

        } catch (IOException ex) {
            System.out.println("Error at client Main method (run()): " + ex.getMessage());
        } catch (ParseException ex) {
            System.out.println("Parse exception erro in switch: "+ex.getMessage());
        }
        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnLogin) {

            try {
                String username = txtUsername.getText();
                String password = String.format(new String(txtPassword.getPassword()));

                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(panel, "Empty field(s) encountered.");
                    return;
                }

                String message = auth + AON + username + AON + password;
                loginWindow.dispose();

                output.writeUTF(message);
                output.flush();

            } catch (IOException ex) {
                System.out.println("btnLogin handler: " + ex.getMessage());
            }

        } else if (e.getSource() == btnRegister) {

            displayRegisterWindow();
            loginWindow.dispose();

        } else if (e.getSource() == btnSubmit) {

            try {
                String regUsername = txtRegUsername.getText();
                String regPass = String.format(new String(txtRegPassword.getPassword()));
                String regPass1 = String.format(new String(txtRegPassword1.getPassword()));
                if (regUsername.equals("") || regPass.equals("") || regPass1.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill empty fields", "Empty fields", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!(regPass.equals(regPass1))) {
                    JOptionPane.showMessageDialog(null, "Password mismatch.Please retype your password", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                output.writeUTF(regUser + AON + regUsername + AON + regPass);
                output.flush();

            } catch (IOException ex) {
                System.out.println("Error at Submit handler: " + ex.getMessage());
            }

        } else if (e.getSource() == btnStart) {
            /*..............Starts the client chat thread...................................................*/

            ChatConnection chatConnection = new ChatConnection();
            chatConnection.openConnection();
            ChatDisplay chatDisplay = new ChatDisplay();
            chatDisplay.setChatDisplay(chatConnection.socket, chatConnection.in, chatConnection.out);
            Thread t = new Thread(chatDisplay);
            t.start();
            launchWindow.dispose();

        } else if (e.getSource() == btnSend) {

            try {
                String message = txtCompose.getText();

                if (message.equals("") || message.equals("Type message here")) {
                    JOptionPane.showMessageDialog(null, "Please type a message");
                    return;
                }

                Date time=Calendar.getInstance().getTime();
                String timeSent=formateDate(time);
                String usertext = mainFrame.getTitle();
                int ind = usertext.indexOf("'");
                String user = usertext.substring(0, ind);
                txtCompose.setText("");

                String sentMessage = chatDisplayMessage + AON + message + AON + user.toLowerCase()+AON+timeSent;

                output.writeUTF(sentMessage);
                output.flush();

            } catch (IOException ex) {
                System.out.println("Error at Message send Text field handler: " + ex.getMessage());
            }

        } else if (e.getSource() == txtCompose) {

            try {
                String message = txtCompose.getText();

                if (message.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please type a message");
                    return;
                }

                Date time=Calendar.getInstance().getTime();
                String timeSent=formateDate(time);
                String usertext = mainFrame.getTitle();
                int ind = usertext.indexOf("'");
                String user = usertext.substring(0, ind);
                txtCompose.setText("");

                String sentMessage = chatDisplayMessage + AON + message + AON + user.toLowerCase()+AON+timeSent;

                output.writeUTF(sentMessage);
                output.flush();

            } catch (IOException ex) {
                System.out.println("Error at Message send Text field handler: " + ex.getMessage());
            }
        } else if (e.getSource() == btnLogout) {

            try {
                int i = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Logout Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (i == 1) {
                    return;
                }

                String usertext = mainFrame.getTitle();
                int ind = usertext.indexOf("'");
                String user = usertext.substring(0, ind);

                String logoutMessage = logout + AON + user.toLowerCase();

                output.writeUTF(logoutMessage);
                output.flush();
                
                displayLauncher();
                mainFrame.dispose();

            } catch (IOException ex) {
                System.out.println("Error at button logout event: " + ex.getMessage());
            }
        } else if (e.getSource() == btnExit) {

            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit this app?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (response == 0) {
                System.exit(0);
            }
        }
    }

    public void logoutListener() {

        try {

            int i = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Logout Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (i == 1) {
                return;
            }

            String usertext = mainFrame.getTitle();
            int ind = usertext.indexOf("'");
            String user = usertext.substring(0, ind);

            String logoutMessage = logout + AON + user.toLowerCase();

            output.writeUTF(logoutMessage);
            output.flush();

            displayLauncher();
            mainFrame.dispose();

        } catch (IOException ex) {
            System.out.println("Error at window logout event: " + ex.getMessage());
        }
    }

    public void loginWindowOut() {
        try {
            int rec = JOptionPane.showConfirmDialog(null, "Do you want to go back to Launcher window?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (rec == 1) {
                return;
            }
            this.displayLauncher();
            loginWindow.dispose();
            output.writeUTF("backlogin");
            output.flush();

        } catch (IOException ex) {
            System.out.println("LoginWindowOut(): " + ex.getMessage());
        }
    }
    
    public String formateDate(Date date) {
        
        DateFormat formatter=new SimpleDateFormat("d,MMM,Y Hmm'hrs'");
        String t=formatter.format(date);
        return t;
    }

}
