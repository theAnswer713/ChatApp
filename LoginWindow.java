import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class LoginWindow implements ActionListener {
    private JTextField ipField, portField, nameField;
    private JFrame frame;

    public LoginWindow() {
        frame = new JFrame("Chat App");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel topLabel = new JLabel("Chat App");
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(topLabel, c);
        c.gridwidth = 1;

        c.gridy = 1;
        JLabel ipLabel = new JLabel("Enter IP:");
        panel.add(ipLabel, c);

        c.gridy = 2;
        JLabel portLabel = new JLabel("Enter Port:");
        panel.add(portLabel, c);

        c.gridy = 3;
        JLabel nameLabel = new JLabel("Enter Name:");
        panel.add(nameLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        ipField = new JTextField(10);
        panel.add(ipField, c);

        c.gridy = 2;
        portField = new JTextField(10);
        panel.add(portField, c);

        c.gridy = 3;
        nameField = new JTextField(10);
        panel.add(nameField, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        JButton connectButton = new JButton("CONNECT");
        connectButton.addActionListener(this);
        panel.add(connectButton, c);

        Font bigFont = new Font("Arial", Font.BOLD, 40);
        topLabel.setFont(bigFont);
        connectButton.setFont(bigFont);

        Font smallFont = new Font("Arial", Font.BOLD, 20);
        ipLabel.setFont(smallFont);
        portLabel.setFont(smallFont);
        nameLabel.setFont(smallFont);
        ipField.setFont(smallFont);
        portField.setFont(smallFont);
        nameField.setFont(smallFont);

        Color darkColor = new Color(50, 50, 50);
        panel.setBackground(darkColor);
        connectButton.setBackground(darkColor);

        Color lightColor = new Color(240, 240, 240);
        topLabel.setForeground(lightColor);
        connectButton.setForeground(lightColor);
        ipLabel.setForeground(lightColor);
        portLabel.setForeground(lightColor);
        nameLabel.setForeground(lightColor);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("Trying to connect...");
            int port = Integer.parseInt(portField.getText());
            Socket socket = new Socket(ipField.getText(), port);
            System.out.println("Connected!");
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Sending username to server.");
            pw.println(nameField.getText());
            frame.dispose();
            new ChatWindow(socket);
        }
        catch(Exception err) {
            err.printStackTrace();
        }
    }
}