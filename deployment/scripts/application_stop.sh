#!/bin/bash

# Stop and remove the Docker container
docker stop currency-api-container || true
docker rm currency-api-container || true
