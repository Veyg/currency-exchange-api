# # Remove the deployment directory (if it exists)
sudo rm -rf /opt/currency-exchange-api || true

# Clean up old Docker containers and images
docker container prune -f
docker image prune -f
