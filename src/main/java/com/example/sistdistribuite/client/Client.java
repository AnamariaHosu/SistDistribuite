package com.example.sistdistribuite.client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private static long totalResponseTime = 0;
    private static int responseCount = 0;
    private static boolean running = true;

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new HeartbeatTask(), 0, 5000);

        try {
            Socket socket = new Socket("192.168.1.2", 9090);
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            OutputStream output = socket.getOutputStream();

            while (running) {
                System.out.print("Enter your name: ");
                String name = input.readLine();
                if (name.equals("bye")) {
                    running = false;
                } else {
                    long startTime = System.currentTimeMillis();
                    output.write(name.getBytes());
                    output.flush();
                    String response = input.readLine();
                    long endTime = System.currentTimeMillis();
                    long responseTime = endTime - startTime;
                    totalResponseTime += responseTime;
                    responseCount++;
                    System.out.println(response);
                }
            }

            timer.cancel();
            double averageResponseTime = (double) totalResponseTime / responseCount;
            System.out.println("Average response time: " + averageResponseTime + "ms");

            input.close();
            output.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            try {
                Socket heartbeatSocket = new Socket("192.168.1.2", 9090);
                OutputStream heartbeatOutput = heartbeatSocket.getOutputStream();
                BufferedReader heartbeatInput = new BufferedReader(new InputStreamReader(heartbeatSocket.getInputStream()));
                heartbeatOutput.write("Heartbeat".getBytes());
                heartbeatOutput.flush();
                String heartbeatResponse = heartbeatInput.readLine();
                System.out.println("Heartbeat response: " + heartbeatResponse);
                heartbeatInput.close();
                heartbeatOutput.close();
                heartbeatSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

