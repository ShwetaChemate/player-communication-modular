package com.unifiedplayer;

/**
 * Model class representing a player which can act as Initiator or Responder.
 */
public class Player {
    private final boolean isInitiator;

    public Player(boolean isInitiator) {
        this.isInitiator = isInitiator;
    }

    public boolean isInitiator() {
        return isInitiator;
    }
}
