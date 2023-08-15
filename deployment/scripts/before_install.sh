# # Remove the deployment directory (if it exists)
rm -rf /opt/currency-exchange-api || true
rm -rf /opt/codedeploy-agent/deployment-root/f758f175-2dce-4cac-a31d-6aa47404eafb || true

# Clean up old Docker containers and images
docker container prune -f
docker image prune -f
