package com.unifiedplayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiProcessCommunicationServiceTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testMainWithNoArgs() throws IOException {
        MultiProcessCommunicationService.main(new String[]{});
        assertTrue(outputStream.toString().contains("Usage in java com.unifiedplayer.MultiProcessCommunicationService"));
    }

    @Test
    void testInitiatorResponderCommunication() throws Exception {
        // Start responder on a new thread
        Thread responderThread = new Thread(() -> {
            try {
                MultiProcessCommunicationService.main(new String[]{"responder"});
            } catch (IOException e) {
                fail("Responder failed: " + e.getMessage());
            }
        });

        responderThread.start();
        Thread.sleep(1000); 

        // Start initiator on main thread
        MultiProcessCommunicationService.main(new String[]{"initiator"});

        responderThread.join();

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Initiator completed sending 10 messages."));
        assertTrue(consoleOutput.contains("Responder shutting down."));
    }
}
