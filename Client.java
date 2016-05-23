/* Group: Dhrumil Patel, Deven Patel, Mitul Patel */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Client extends Thread {
    private final Client[] clients;
    private String clientName = null;
    private PrintStream outputStream = null;
    private Socket clientSocket = null;
    private int maxClient;

    public Client(Socket clientSocket, Client[] clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        maxClient = clients.length;
    }

    public void run() {
        int maxClientsCount = this.maxClient;
        Client[] threads = this.clients;

        try {

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
             outputStream = new PrintStream(clientSocket.getOutputStream());
            String name = getName(inputStream);

            outputStream.println("Welcome " + name + " to chat room.\nTo leave enter QUIT in a new line." +
                    "\nTo Send a private message type '@username message' and press send.\n" +
                    "To broadcast a message type 'message' and press send.\n" +
                    "To send to multiple people type @username1 @username2 message and press send.");

            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] == this) {
                        clientName = name;
                        break;
                    }
                }
                ArrayList<String> currentUsers = new ArrayList<>();
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].outputStream.println("-- A new user " + name
                                + " entered the chat room  --");
                        currentUsers.add(threads[i].clientName);
                    }
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null
                            && threads[i].clientName != null) {
                        threads[i].outputStream.println("META:" + String.join(",", currentUsers));
                    }
                }
            }
            while (true) {
                String line = inputStream.readLine();
                if (line.startsWith("QUIT")) {
                    break;
                }
                if (line.startsWith("@")) {
                    List<String> namesToSend = new ArrayList<>();
                    String[] words = line.split("\\s", 2);
                    while (line.startsWith("@")) {
                         words = line.split("\\s", 2);
                        namesToSend.add(words[0]);
                        line = words[1];
                    }
                    System.out.println(namesToSend);
                    if (words.length > 1 && words[1] != null) {
                        words[1] = words[1].trim();
                        if (!words[1].isEmpty()) {
                            synchronized (this) {
                                boolean found = false;
                                for (int i = 0; i < maxClientsCount; i++) {

                                    for(String na: namesToSend) {
                                        System.out.println(na);
                                        if (threads[i] != null && threads[i] != this
                                                && threads[i].clientName != null
                                                && threads[i].clientName.equals(na.substring(1))) {
                                            threads[i].outputStream.println(name + ": " + words[1]);
found = true;
                                        }
                                    }
                                }
                                if(found)
                                    this.outputStream.println(name + ": " + words[1]);

                            }
                        }
                    }
                } else {
                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null) {
                                threads[i].outputStream.println( name + ":" + line);
                            }
                        }
                    }
                }
            }
            synchronized (this) {
                ArrayList<String> currentUsers = new ArrayList<>();
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this
                            && threads[i].clientName != null) {
                        threads[i].outputStream.println("--- The user " + name + " is leaving the chat room ---");
                        currentUsers.add(threads[i].clientName);
                    }
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this
                            && threads[i].clientName != null) {
                        threads[i].outputStream.println("META:" + String.join(",", currentUsers));
                    }
                }
            }


            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException ignored) {
        }
    }

    private String getName(DataInputStream is) throws IOException {
        String name;
        while (true) {
            name = is.readLine().trim();
            if(name.length()>0)
                break;
        }
        return name;
    }
}
