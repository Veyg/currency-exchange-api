#!/bin/bash
set -e

# Stop and remove existing Docker containers
docker stop currency-exchange-api-container || true
docker rm currency-exchange-api-container || true
