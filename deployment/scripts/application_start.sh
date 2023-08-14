#!/bin/bash

# Extract the Docker image
tar -xzvf /opt/currency-exchange-api/docker-image.tar.gz -C /opt/currency-exchange-api/

# Load the Docker image
docker load -i /opt/currency-exchange-api/currency-exchange-api.tar

# Run the Docker container
docker run -d -p 8080:8080 --name currency-api-container currency-exchange-api
