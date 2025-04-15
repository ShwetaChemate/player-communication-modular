package com.unifiedplayer;

public class SingleProcessCommunicationService implements Runnable {

    private final Player player;
    private final CommunicationService.InMemoryChannel channel;

    public SingleProcessCommunicationService(Player player, CommunicationService.InMemoryChannel channel) {
        this.player = player;
        this.channel = channel;
    }

    @Override
    public void run() {
        long pid = ProcessHandle.current().pid();
        if (player.isInitiator()) {
            System.out.println("Running InitiatorPlayer with PID: " + pid);
            StringBuilder history = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                String message = "message#" + i;
                history.append(message);
                channel.sendToResponder(history.toString());
                String response = channel.receiveFromResponder();
                System.out.println("Initiator received: " + response);
                history = new StringBuilder(response);
            }
            System.out.println("Initiator completed sending 10 messages.");
        } else {
            System.out.println("Running ResponderPlayer with PID: " + pid);
            int count = 1;
            while (count <= 10) {
                String message = channel.receiveFromInitiator();
                System.out.println("Responder received: " + message);
                String response = message + "|reply#" + count;
                channel.sendToInitiator(response);
                count++;
            }
            System.out.println("Responder shutting down.");
        }
    }
}
