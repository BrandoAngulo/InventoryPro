# Stage 1: Build stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy Maven dependency definitions and download dependencies to optimize caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline -B || mvn dependency:go-offline -B

# Copy source code and build the application executable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Stage 2: Production runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user and group for enhanced security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser:appgroup

# Copy compiled JAR artifact from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default application port
EXPOSE 8080

# Configure container execution
ENTRYPOINT ["java", "-jar", "app.jar"]
