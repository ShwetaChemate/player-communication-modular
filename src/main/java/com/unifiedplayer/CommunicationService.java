package com.unifiedplayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommunicationService {
    public static class InMemoryChannel {
        private final BlockingQueue<String> initiatorToResponder = new LinkedBlockingQueue<>();
        private final BlockingQueue<String> responderToInitiator = new LinkedBlockingQueue<>();

        public void sendToResponder(String msg) {
            initiatorToResponder.add(msg);
        }

        public String receiveFromInitiator() {
            try {
                return initiatorToResponder.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        public void sendToInitiator(String msg) {
            responderToInitiator.add(msg);
        }

        public String receiveFromResponder() {
            try {
                return responderToInitiator.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }
}
