package com.unifiedplayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SingleProcessCommunicationServiceTest {

    private CommunicationService.InMemoryChannel channel;
    private Player initiator;
    private Player responder;
    private SingleProcessCommunicationService initiatorService;
    private SingleProcessCommunicationService responderService;

    @BeforeEach
    void setUp() {
        channel = mock(CommunicationService.InMemoryChannel.class);
        
        // Creating mock players
        initiator = new Player(true); // Initiator
        responder = new Player(false); // Responder
        
        initiatorService = new SingleProcessCommunicationService(initiator, channel);
        responderService = new SingleProcessCommunicationService(responder, channel);
    }

    @Test
    void testInitiatorSendsAndReceivesMessages() throws InterruptedException {
        // Mock channel behavior for the initiator
        when(channel.receiveFromResponder()).thenReturn("message#1|reply#1|", "message#2|reply#2|", "message#3|reply#3|", 
                "message#4|reply#4|", "message#5|reply#5|", "message#6|reply#6|", "message#7|reply#7|", 
                "message#8|reply#8|", "message#9|reply#9|", "message#10|reply#10|");

        // Run the initiator and responder service in separate threads
        Thread initiatorThread = new Thread(initiatorService);
        Thread responderThread = new Thread(responderService);
        
        responderThread.start();
        initiatorThread.start();

        initiatorThread.join();
        responderThread.join();

        // Verify that the channel interaction is correct
        verify(channel, times(10)).sendToResponder(anyString()); // initiator sends messages
        verify(channel, times(10)).receiveFromResponder();     // initiator receives responses
    }

    @Test
    void testResponderReceivesAndSendsMessages() throws InterruptedException {
        // Mock channel behavior for the responder
        when(channel.receiveFromInitiator()).thenReturn("message#1|", "message#2|", "message#3|", 
                "message#4|", "message#5|", "message#6|", "message#7|", "message#8|", "message#9|", "message#10|");

        Thread initiatorThread = new Thread(initiatorService);
        Thread responderThread = new Thread(responderService);

        responderThread.start();
        initiatorThread.start();

        initiatorThread.join();
        responderThread.join();

        verify(channel, times(10)).receiveFromInitiator(); // responder receives messages
        verify(channel, times(10)).sendToInitiator(anyString()); // responder sends replies
    }
}
