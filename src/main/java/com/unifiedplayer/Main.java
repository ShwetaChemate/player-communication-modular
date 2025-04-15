package com.unifiedplayer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage in java com.unifiedplayer.Main <single|multi>");
            return;
        }

        //switch case for single and multi process modes
        switch (args[0]) {
            case "single":
                CommunicationService.InMemoryChannel channel = new CommunicationService.InMemoryChannel();
                Thread responderThread = new Thread(new SingleProcessCommunicationService(new Player(false), channel));
                Thread initiatorThread = new Thread(new SingleProcessCommunicationService(new Player(true), channel));
                responderThread.start();
                initiatorThread.start();
                try {
                    responderThread.join();
                    initiatorThread.join();
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt(); // reset interrupted status
                }
                break;

            case "multi":
                System.out.println("Launching Responder and Initiator via run_players.sh shell script");
                try {
                    ProcessBuilder pb = new ProcessBuilder("sh", "scripts/run_players.sh");
                    pb.inheritIO(); 
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    System.out.println("Process finished with exit code: " + exitCode);

                } catch (IOException e) {
                    System.err.println("Failed to run shell script: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
