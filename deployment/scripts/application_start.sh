#!/bin/bash

# Define paths and image name
IMAGE_TAR="/opt/currency-exchange-api/docker-image.tar.gz"
EXTRACT_DIR="/opt/currency-exchange-api"
IMAGE_NAME="veyg/currency-exchange-api"
LOAD_IMG="docker-image.tar.gz"

# Check if the image tar file exists
if [ ! -f "$IMAGE_TAR" ]; then
    echo "Error: Docker image tar file not found at $IMAGE_TAR"
    exit 1
fi

# Load the Docker image
echo "Loading Docker image..."
docker load -i "$EXTRACT_DIR/$LOAD_IMG"

# Run the Docker container
echo "Starting Docker container..."
docker run -d -p 8080:8080 -p 3306:3306 --name currency-api-container "$IMAGE_NAME" --env-file /opt/currency-exchange-api/.env

# Check if the container is running
if [ "$(docker ps -q -f name=currency-api-container)" ]; then
    echo "Docker container started successfully"
else
    echo "Error: Docker container failed to start"
    exit 1
fi
