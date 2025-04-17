import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatWindow implements ActionListener {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private JTextArea chatArea, userArea;
    private JTextField sendField;

    public ChatWindow(Socket socket) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception err) {
            err.printStackTrace();
        }

        JFrame frame = new JFrame("Chat App");
        Font font = new Font("Arial", Font.PLAIN, 30);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        chatArea = new JTextArea();
        JScrollPane chatPane = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatPane.setPreferredSize(new Dimension(600, 500));
        chatArea.setFont(font);
        chatArea.setLineWrap(true);
        chatArea.setEditable(false);
        centerPanel.add(chatPane, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        userArea = new JTextArea();
        JScrollPane userPane = new JScrollPane(userArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        eastPanel.setPreferredSize(new Dimension(200, 500));
        userArea.setLineWrap(true);
        userArea.setFont(font);
        userArea.setEditable(false);
        eastPanel.add(userPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        sendField = new JTextField();
        sendField.setFont(font);
        sendField.setPreferredSize(new Dimension(800, 100));
        sendField.addActionListener(this);
        southPanel.add(sendField, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(eastPanel, BorderLayout.EAST);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread listenThread = new Thread(new ListenThread());
        listenThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = sendField.getText();
        pw.println(message);
        sendField.setText("");
    }

    private class ListenThread implements Runnable {
        public void run() {
            try {
                while (!socket.isClosed()) {
                    String message = br.readLine();
                    System.out.println(message);
                    if(message.startsWith("USERS")) {
                        message = message.replaceAll("/","\n");
                        userArea.setText(message);
                    }
                    else {
                        chatArea.append(message+"\n");
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}