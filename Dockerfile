# Stage 1: Build the Spring Boot application
FROM maven:3.9.9-openjdk-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all project files
COPY . .

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR using a lightweight image
FROM openjdk:21-jdk-slim

# Copy the built JAR file from the build stage
COPY --from=build /app/target/best_Store_SB-0.01-SNAPSHOT.jar best_Store_SB.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "best_Store_SB.jar"]
