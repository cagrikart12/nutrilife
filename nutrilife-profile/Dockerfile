# Multi-stage build
FROM maven:3.9.5-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Install necessary packages
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r nutrilife && useradd -r -g nutrilife nutrilife

# Copy the built jar from build stage
COPY --from=build /app/target/nutrilife-profile-*.jar app.jar

# Change ownership to non-root user
RUN chown -R nutrilife:nutrilife /app
USER nutrilife

# Expose port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8082/api/profiles/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
