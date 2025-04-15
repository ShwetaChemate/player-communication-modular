#!/bin/bash

PROJECT_DIR=$(pwd)
CLASS_PATH="target/classes"
MAIN_CLASS="com.unifiedplayer.MultiProcessCommunicationService"

# Function to run command in new terminal
run_in_new_terminal() {
    local CMD=$1
    local TITLE=$2

    case "$OSTYPE" in
    darwin*) # macOS
        osascript <<EOF
tell application "Terminal"
  do script "cd \"$PROJECT_DIR\"; java -cp $CLASS_PATH $MAIN_CLASS $CMD"
end tell
EOF
        ;;
    linux*) # Linux
        gnome-terminal -- bash -c "cd \"$PROJECT_DIR\"; java -cp $CLASS_PATH $MAIN_CLASS $CMD; exec bash" ||
            x-terminal-emulator -e bash -c "cd \"$PROJECT_DIR\"; java -cp $CLASS_PATH $MAIN_CLASS $CMD; exec bash"
        ;;
    msys* | cygwin* | win*) # Windows Git Bash or WSL
        cmd.exe /C "start cmd /K \"cd /d $PROJECT_DIR && java -cp $CLASS_PATH $MAIN_CLASS $CMD\""
        ;;
    *)
        echo "Unsupported OS: $OSTYPE"
        exit 1
        ;;
    esac
}

# Compile Java classes
echo "Compiling Java classes..."
mvn compile

echo "Starting Responder..."
run_in_new_terminal responder "Responder"

sleep 2

echo "Starting Initiator..."
run_in_new_terminal initiator "Initiator"
