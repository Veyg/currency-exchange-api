#!/bin/bash

# Define paths and image name
IMAGE_TAR="/opt/currency-exchange-api/docker-image.tar.gz"
EXTRACT_DIR="/opt/currency-exchange-api"
IMAGE_NAME="currency-exchange-api"

# Check if the image tar file exists
if [ ! -f "$IMAGE_TAR" ]; then
    echo "Error: Docker image tar file not found at $IMAGE_TAR"
    exit 1
fi

# Extract the Docker image
echo "Extracting Docker image..."
tar -xzvf "$IMAGE_TAR" -C "$EXTRACT_DIR"

# Load the Docker image
echo "Loading Docker image..."
docker load -i "$EXTRACT_DIR/$IMAGE_NAME.tar"

# Run the Docker container
echo "Starting Docker container..."
docker run -d -p 8080:8080 --name currency-api-container "$IMAGE_NAME"

# Check if the container is running
if [ "$(docker ps -q -f name=currency-api-container)" ]; then
    echo "Docker container started successfully"
else
    echo "Error: Docker container failed to start"
    exit 1
fi
