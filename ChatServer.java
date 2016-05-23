/* Group: Dhrumil Patel, Deven Patel, Mitul Patel */

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer {
    private static final int maxClients = 20;
    private static final Client[] clients = new Client[maxClients];
    private static ServerSocket serverSocket = null;

    public static void main(String args[]) {

        int portNumber = 2337;

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClients; i++) {
                    if (clients[i] == null) {
                        (clients[i] = new Client(clientSocket, clients)).start();
                        break;
                    }
                }
                if (i == maxClients) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server is too busy. Please try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}
