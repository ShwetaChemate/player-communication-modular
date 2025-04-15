package com.unifiedplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CommunicationServiceTest {

    @Test
    void testInitiatorToResponderCommunication() {
        CommunicationService.InMemoryChannel channel = new CommunicationService.InMemoryChannel();
        String message = "Hello Responder!";
        channel.sendToResponder(message);
        String received = channel.receiveFromInitiator();
        assertEquals(message, received, "Responder should receive the exact message sent by Initiator.");
    }

    @Test
    void testResponderToInitiatorCommunication() {
        CommunicationService.InMemoryChannel channel = new CommunicationService.InMemoryChannel();
        String message = "Hello Initiator!";
        channel.sendToInitiator(message);
        String received = channel.receiveFromResponder();
        assertEquals(message, received, "Initiator should receive the exact message sent by Responder.");
    }

    @Test
    void testMessageOrderPreservation() {
        CommunicationService.InMemoryChannel channel = new CommunicationService.InMemoryChannel();
        channel.sendToResponder("msg1");
        channel.sendToResponder("msg2");
        assertEquals("msg1", channel.receiveFromInitiator());
        assertEquals("msg2", channel.receiveFromInitiator());
    }

    @Test
    void testBlockingBehavior() {
        CommunicationService.InMemoryChannel channel = new CommunicationService.InMemoryChannel();

        Thread receiver = new Thread(() -> {
            String msg = channel.receiveFromInitiator();
            assertEquals("delayed message", msg);
        });

        receiver.start();

        try {
            Thread.sleep(200);  
            channel.sendToResponder("delayed message");
            receiver.join(1000); 
        } catch (InterruptedException e) {
            fail("Thread was interrupted.");
        }
    }
}
