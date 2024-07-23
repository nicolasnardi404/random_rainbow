# Stage 1: Build the application
FROM maven:3.8.6 AS builder

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the rest of the application code
COPY src ./src

# Compile the application
RUN mvn clean package

# Stage 2: Prepare the final image
FROM openjdk:17-jdk-slim-arm64

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Make port 8080 available outside the container
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
