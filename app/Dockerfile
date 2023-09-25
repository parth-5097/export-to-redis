# Dockerfile for running unit tests for ExportDataToRedis
# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the compiled JAR file into the container at /app
COPY ExportDataToRedis.jar /app

# Copy the JUnit library JAR file into the container at /app
COPY junit-4.13.1.jar /app

# Run the unit tests
CMD ["java", "-cp", ".:ExportDataToRedis.jar:junit-4.13.1.jar", "org.junit.runner.JUnitCore", "ExportDataToRedisTest"]
