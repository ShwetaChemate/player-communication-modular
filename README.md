```markdown
# Player Communication Project

This project simulates communication between two players in both **single-process** and **multi-process** modes. The players can act as either an **Initiator** or a **Responder**. The communication is handled using sockets in multi-process mode and in-memory channels in single-process mode.

## Features

- **Single-process mode**: Players communicate using in-memory channels.
- **Multi-process mode**: Players communicate over a network using sockets.
- **Initiator-Responder Communication**: The Initiator sends a series of messages, and the Responder replies with the appropriate responses.
- **Threaded Communication**: For single-process mode, the communication is handled by separate threads for each player.

## Prerequisites

- Java 11 or higher
- Apache Maven 3.6 or higher

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/player-communication.git
   cd player-communication
   ```

2. Install dependencies using Maven:

   ```bash
   mvn install
   ```

## Running the Application

### Single-Process Mode

In this mode, both players run in the same process and communicate using in-memory channels.

1. To run the Initiator and Responder in **single-process mode**, execute:

   ```bash
   java -cp target/classes com.unifiedplayer.Main single
   ```

   This will start both the **Initiator** and **Responder** on separate threads.

### Multi-Process Mode

In this mode, players run in different processes and communicate over a network using sockets.

1. First, run the **Responder** process:

   ```bash
   java -cp target/classes com.unifiedplayer.MultiProcessCommunicationService responder
   ```

2. Then, in another terminal, run the **Initiator** process:

   ```bash
   java -cp target/classes com.unifiedplayer.MultiProcessCommunicationService initiator
   ```

   Alternatively, you can run both processes using the provided shell script:

   ```bash
   sh run_players.sh
   ```

## How it Works

### Single-Process Mode
- The **Initiator** sends a series of messages to the **Responder** via an in-memory communication channel.
- The **Responder** receives the messages and sends back replies, which are received by the **Initiator**.

### Multi-Process Mode
- The **Initiator** and **Responder** communicate over TCP sockets on a specified port (`5001`).
- The **Initiator** connects to the **Responder** via a socket and sends messages. The **Responder** replies back to the **Initiator**.

## Testing

To run the tests for the project, use the following Maven command:

```bash
mvn test
```

### Test Case Coverage

- **Player Class**: Verifies the correct initialization of the player as **Initiator** or **Responder**.
- **CommunicationService**: Ensures that messages are sent and received correctly in both single and multi-process modes.
- **MultiProcessCommunicationService**: Tests the socket-based communication for both **Initiator** and **Responder**.
- **SingleProcessCommunicationService**: Verifies the behavior of single-process communication using in-memory channels.

## Troubleshooting

- If you encounter an error related to missing dependencies, ensure you have added the required dependencies for **Mockito** and **JUnit** in your `pom.xml`.
- Make sure the port `5001` is not being used by other processes when running in multi-process mode.

