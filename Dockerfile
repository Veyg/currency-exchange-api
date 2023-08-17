# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim
# Set the timezone to UTC
ENV TZ=UTC

# Create a directory to copy the application JAR and resources
WORKDIR /app

# Copy the application JAR
COPY target/*.jar app.jar

# Copy the .env file to the container
COPY .env .env

# Expose port 8080 for the application
EXPOSE 8080

# Expose port 3306 for the database
EXPOSE 3306

# Run the application with environment variables from the .env file
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
