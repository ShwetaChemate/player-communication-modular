package com.unifiedplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

//This service manages the communication between the initiator and responder in multi-process mode.
public class MultiProcessCommunicationService {

    private final Player player;
    private final int port = 5001;

    public MultiProcessCommunicationService(Player player) {
        this.player = player;
    }

    public void start() throws IOException {
        long pid = ProcessHandle.current().pid();
        if (player.isInitiator()) {
            System.out.println("Running InitiatorPlayer with PID: " + pid);
            try (Socket socket = new Socket("localhost", port);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                StringBuilder history = new StringBuilder();
                for (int i = 1; i <= 10; i++) {
                    String message = "message#" + i + "|";
                    history.append(message);
                    writer.write(history.toString());
                    writer.newLine();
                    writer.flush();
                    String response = reader.readLine();
                    System.out.println("Initiator received: " + response);
                    history = new StringBuilder(response);
                }
                System.out.println("Initiator completed sending 10 messages.");
            }
        } else {
            System.out.println("Running ResponderPlayer with PID: " + pid);
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Responder is listening on port " + port + "...");
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    int count = 1;
                    String message;
                    while ((message = reader.readLine()) != null && count <= 10) {
                        System.out.println("Responder received: " + message);
                        String response = message + "reply#" + count + "|";
                        writer.write(response);
                        writer.newLine();
                        writer.flush();
                        count++;
                    }
                    System.out.println("Responder shutting down.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage in java com.unifiedplayer.MultiProcessCommunicationService <initiator|responder>");
            return;
        }

        boolean isInitiator = args[0].equalsIgnoreCase("initiator");
        Player player = new Player(isInitiator);
        MultiProcessCommunicationService service = new MultiProcessCommunicationService(player);
        service.start();
    }
}
