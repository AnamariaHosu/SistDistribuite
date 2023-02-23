package com.example.server.Server;
import java.net.*;
import java.io.*;

public class MultiThreadServer implements Runnable {
    private Socket socket;
    private int clientNumber;
    public MultiThreadServer(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        log("New client #" + clientNumber + " connected at " + socket);
    }

    public void run() {
        try {
            handleRequest();
        } catch (Exception e) {
            log("Error handling client #" + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                log("Error closing client #" + clientNumber + " socket: " + e);
            }
            log("Connection with client #" + clientNumber + " closed");
        }
    }

    private void handleRequest() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Get messages from the client
        while (true) {
            String input = in.readLine();
            if (input == null || input.equals(".")) {
                break;
            }
            if (input == "Heartbeat"){
                out.println("OK");
                socket.close();
                return;
            }
            log(input);
            out.println(input);
        }
    }
    private static void log(String message) {
        System.out.println(message);
    }
    public static void main(String[] args) throws Exception {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Server running on IP address: " + ip.getHostAddress());
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9090);
        log("Server is listening on port 9090");
        try {
            while (true) {
                new Thread(new MultiThreadServer(listener.accept(), clientNumber++)).start();
            }
        } finally {
            listener.close();
        }
    }
}
