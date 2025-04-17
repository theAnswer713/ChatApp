import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.LinkedHashSet;

public class ChatServer {
    private ServerSocket server;
    private Set<ClientData> clientSet;

    public static void main(String[] args) throws Exception {
        new ChatServer();
    }

    public ChatServer() throws Exception {
        this.server = new ServerSocket(55555);
        this.clientSet = new LinkedHashSet<ClientData>();

        Thread acceptThread = new Thread(new AcceptThread());
        acceptThread.start();
        acceptThread.join();
    }

    private class AcceptThread implements Runnable {
        public void run() {
            try {
                while(!server.isClosed()) {
                    System.out.println("Waiting for someone to connect...");
                    Socket socket = server.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String username = br.readLine();
                    String message = username+" has connected to the server!";
                    ClientData cd = new ClientData(socket, username);
                    clientSet.add(cd);
                    System.out.println(message);
                    for(ClientData x:clientSet) {
                        x.getPw().println(message);
                    }

                    Thread listenThread = new Thread(new ListenThread(cd));
                    listenThread.start();

                    String users = "USERS:";
                    for(ClientData x:clientSet) {
                        users+="/"+x.getUsername();
                    }
                    for(ClientData x:clientSet) {
                        x.getPw().println(users);
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ListenThread implements Runnable {
        private ClientData cd;

        public ListenThread(ClientData cd) {
            this.cd = cd;
        }

        public void run() {
            try{
                while(!cd.getSocket().isClosed()) {
                    String message = cd.getBr().readLine();
                    message = "["+cd.getUsername()+"]: "+message;
                    System.out.println(message);
                    for(ClientData x:clientSet) {
                        x.getPw().println(message);
                    }
                }
            }
            catch(Exception err) {
                String message = cd.getUsername()+" has left the server.";
                System.out.println(message);
                for(ClientData x:clientSet) {
                    x.getPw().println(message);
                }
                clientSet.remove(cd);
                String users = "USERS:";
                for(ClientData x:clientSet) {
                    users+="/"+x.getUsername();
                }
                for(ClientData x:clientSet) {
                    x.getPw().println(users);
                }
            }
        }
    }
}