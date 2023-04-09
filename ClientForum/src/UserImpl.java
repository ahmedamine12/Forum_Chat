import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class UserImpl extends UnicastRemoteObject implements User, ActionListener
{
    private Forum forum;
    private int id;
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton enterButton;
    private JFrame enterFrame;
    private JMenuBar menuBar;
    private JMenu qui;
    private JMenu quitter;
    public UserImpl(Forum forum) throws RemoteException {
        this.forum = forum;
        buildGUI();
    }

    public int getId() {
		return id;
	}

	public Proxy getProxy() {
        try {
            return new ProxyImpl(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void ecrire(String msg) {
        chatArea.append(msg + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    	 if (e.getSource() == enterButton)
         {
            try {
                // Call the 'entrer' method on the server to connect the user to the forum
                id = forum.entrer(getProxy());
                frame.setTitle("✱✱✱ Chat Forum - User " + id +" ✱✱✱ ");

                enterFrame.setVisible(false);
                frame.setVisible(true); // show the main chat frame
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else {
            String message = inputField.getText();
            if(!message.equals("")) {
                inputField.setText("");
                try {
                    forum.dire(id, message);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void buildGUI() throws RemoteException
    {

        // Create the main frame
        frame = new JFrame("Forum - User");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options ▼");
        fileMenu.setForeground(Color.BLUE);
        fileMenu.setBackground(new Color(66, 139, 202)); // Use a shade of blue that complements the chat area

// Create the "down" icon and set it as the menu item's icon
        ImageIcon downIcon = new ImageIcon("down-arrow.png");
        JLabel downLabel = new JLabel(downIcon);
        fileMenu.add(downLabel);

        JMenuItem quiItem = new JMenuItem("Qui?");
        quiItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JOptionPane.showMessageDialog(frame, forum.qui());
                } catch (HeadlessException | RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem quitterItem = new JMenuItem("Quitter");
        quitterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    forum.quiter(id);
                    JOptionPane.showMessageDialog(frame, "user: " + id + " confirmer votre operation");
                    enterFrame.setVisible(true);
                    frame.setVisible(false);
                } catch (HeadlessException | RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fileMenu.add(quiItem);
        fileMenu.add(quitterItem);
        menuBar.add(fileMenu);

        // Set the JMenuBar to the frame
        frame.setJMenuBar(menuBar);
        // Generale style of fonts
        final Font INPUT_FONT = new Font("Arial", Font.BOLD, 13);


        // Create the panel for the chat area and input field
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setForeground(Color.BLACK);
        chatArea.setFont(INPUT_FONT);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField(100);
        inputField.setPreferredSize(new Dimension(400, 30));
        inputField.setFont(INPUT_FONT);
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding to the input field
        inputField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        inputField.setToolTipText("Ecrire votre message ici...");
        inputField.addActionListener(this);



        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the panel
        inputPanel.add(inputField, BorderLayout.CENTER);
        JButton envoyerButton = new JButton();
        ImageIcon sendIcon = new ImageIcon("assets/send-message.png");
        Image sendImage = sendIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon smallSendIcon = new ImageIcon(sendImage);
        envoyerButton.setIcon(smallSendIcon);
        envoyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                inputField.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
        });

// Add the button to the input panel
        inputPanel.add(envoyerButton, BorderLayout.EAST);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(chatPanel, BorderLayout.CENTER);



        // Create the frame for the "Enter" button
        enterFrame = new JFrame("Enter");

// Load the background image from file
        ImageIcon backgroundImageIcon = new ImageIcon("assets/Background.png");

// Scale the background image to fit the size of the JLabel component
        Image scaledBackgroundImage = backgroundImageIcon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImageIcon = new ImageIcon(scaledBackgroundImage);

// Create a JLabel component to display the scaled background image
        JLabel backgroundImageLabel = new JLabel(scaledBackgroundImageIcon);
        backgroundImageLabel.setBounds(0, 0, 700, 500);

// Add the background image to the enterFrame
        enterFrame.add(backgroundImageLabel);

// Add the title label to the enterFrame
        JLabel titleLabel = new JLabel("Bienvenue dans ce forum de chat");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        enterFrame.add(titleLabel, BorderLayout.NORTH);

// Create a JPanel to hold the enter button and label
        JPanel enterPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Cliquer sur 'entrer' pour participer au chat");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(INPUT_FONT);
        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);
        enterButton.setFont(INPUT_FONT);
        enterPanel.add(label, BorderLayout.CENTER);
        enterPanel.add(enterButton, BorderLayout.SOUTH);

// Add the enter panel to the enterFrame
        enterFrame.add(enterPanel, BorderLayout.SOUTH);

        enterFrame.setSize(700, 500);
        enterFrame.setLocationRelativeTo(null);
        enterFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        enterFrame.setVisible(true);


        // Set the frame size and visibility
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

    }

}