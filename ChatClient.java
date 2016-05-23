/* Group: Dhrumil Patel, Deven Patel, Mitul Patel */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient {

    public static void main(String[] args) throws IOException {
        String server = "localhost";
        int port = 2222;
        Socket socket = new Socket(server, port);
        OutputStream outputStream = socket.getOutputStream();

        Thread receivingThread = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String line;

                    while (true) {
                        line = reader.readLine();
                        if (line == null) break;
                        System.out.println(line);
                        Thread.sleep(1000);
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        };
        receivingThread.start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            String line = sc.nextLine();
            outputStream.write((line + "\n").getBytes());
            outputStream.flush();
        }
    }
}