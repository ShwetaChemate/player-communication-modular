package com.unifiedplayer;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java com.unifiedplayer.Main <single|multi>");
            return;
        }

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
                    e.printStackTrace();
                }
                break;
            case "multi":
                System.out.println("Use terminal to run separate processes for each player.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
