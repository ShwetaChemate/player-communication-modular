package com.unifiedplayer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testPlayerAsInitiator() {
        Player player = new Player(true);
        assertTrue(player.isInitiator(), "Player should be an initiator");
    }

    @Test
    void testPlayerAsResponder() {
        Player player = new Player(false);
        assertFalse(player.isInitiator(), "Player should be a responder");
    }
}
