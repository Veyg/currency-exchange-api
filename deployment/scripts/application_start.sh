#!/bin/bash
# Start the Java API application
echo "Loading env variables"
source .env
echo "Starting the currency-exchange-api application"
java -jar /opt/currency-exchange-api/currency-exchange-api-1.0.0.jar

# Exit code 0 indicates success. Exit code 1 indicates failure.
exit 0