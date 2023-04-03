import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UserImpl extends UnicastRemoteObject implements User, ActionListener {
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

    public void setId(int id) {
        this.id = id;
        frame.setTitle("Forum - User " + id);
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
    	//e.getSource().equals(menuBar.getMenu(0))
    	System.out.println(e.getSource().toString());
    	if(e.getSource()==qui) {
    		try {
				JOptionPane.showMessageDialog(frame,forum.qui());
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	else if(e.getSource()==quitter) {
    		try {
    			//forum.quiter(id);
    			//JOptionPane.showMessageDialog(frame,"user: "+id+" a quitt�");
                	System.out.println("Qui menu item clicked");

    			} catch (HeadlessException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	}
    	else if (e.getSource() == enterButton) {
            try {
                // Call the 'entrer' method on the server to connect the user to the forum
                id = forum.entrer(getProxy());
                frame.setTitle("Forum - User " + id);

                enterFrame.setVisible(false);
                frame.setVisible(true); // show the main chat frame
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else {
            String message = inputField.getText();
            inputField.setText("");
            try {
                forum.dire(id, message);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void buildGUI() throws RemoteException {

        // Create the main frame
        frame = new JFrame("Forum - User");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a JMenuBar
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options");
        fileMenu.setForeground(Color.BLACK);
        fileMenu.setBackground(Color.BLACK);

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

        // Create the panel for the chat area and input field
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField(100);
        inputField.setPreferredSize(new Dimension(400, 30));

        inputField.setFont(new Font("Arial", Font.BOLD, 13));
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        inputField.setMargin(new Insets(5, 5, 5, 5));
        inputField.setToolTipText("Type your message here");
        inputField.addActionListener(this);

        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        JButton envoyerButton = new JButton("Envoyer");
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
        enterFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel enterPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Click the 'Enter' button to join the chat!");
        label.setHorizontalAlignment(JLabel.CENTER);

        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);

        enterPanel.add(label, BorderLayout.CENTER);
        enterPanel.add(enterButton, BorderLayout.SOUTH);

        enterFrame.add(enterPanel);
        enterFrame.setSize(400, 200); // set the size of the enterFrame

        // Set the frame size and visibility
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        enterFrame.setLocationRelativeTo(null);
        enterFrame.setVisible(true);
    }

}