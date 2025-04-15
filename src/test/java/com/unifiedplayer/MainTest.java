package com.unifiedplayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void testNoArgumentsProvided() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});  // No arguments

        assertTrue(outContent.toString().contains("Usage in java com.unifiedplayer.Main <single|multi>"));
    }

    @Test
    public void testInvalidArgument() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{"invalid"});

        assertTrue(outContent.toString().contains("Invalid option."));
    }

    @Test
    public void testMultiModeScriptInvocation() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Using a dummy shell script to simulate success
        try {
            String script = "run_players.sh"; // temporary script
            java.nio.file.Files.write(java.nio.file.Paths.get(script),
                    "#!/bin/sh\necho 'Running shell script'\nexit 0\n".getBytes());
            new java.io.File(script).setExecutable(true);

            Main.main(new String[]{"multi"});

            assertTrue(outContent.toString().contains("Launching Responder and Initiator via run_players.sh"));
            assertTrue(outContent.toString().contains("Process finished with exit code: 0"));

            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(script));
        } catch (IOException | InterruptedException e) {
            System.err.println("Test failed due to: " + e.getMessage());
        }
    }
}

