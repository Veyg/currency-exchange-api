#!/bin/bash

# Stop and remove the previous Docker container (if it exists)
docker stop currency-api-container || true
docker rm currency-api-container || true

# # Remove the deployment directory (if it exists)
rm -rf /opt/currency-exchange-api || true
rm -rf /opt/codedeploy-agent/deployment-root/f758f175-2dce-4cac-a31d-6aa47404eafb || true