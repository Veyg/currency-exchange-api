#!/bin/bash

# Find the process ID (PID) of the running Java application
PID=$(pgrep -f "java -jar /opt/currency-exchange-api/currency-exchange-api.jar")

if [ -z "$PID" ]; then
  echo "No running Java application found."
else
  echo "Stopping Java application with PID $PID..."
  kill "$PID"
  sleep 5
  echo "Java application stopped."
fi
