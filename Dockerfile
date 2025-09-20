# Multi-stage build for all microservices
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy parent pom.xml
COPY pom.xml .

# Copy all module pom.xml files
COPY nutrilife-core/pom.xml nutrilife-core/
COPY nutrilife-auth/pom.xml nutrilife-auth/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code for all modules
COPY nutrilife-core/src nutrilife-core/src
COPY nutrilife-auth/src nutrilife-auth/src

# Build all modules
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Copy built jars
COPY --from=build /app/nutrilife-core/target/*.jar core.jar
COPY --from=build /app/nutrilife-auth/target/*.jar auth.jar

# Expose ports
EXPOSE 8080 8081

# Create startup script
RUN echo '#!/bin/sh' > start.sh && \
    echo 'java -jar core.jar &' >> start.sh && \
    echo 'java -jar auth.jar &' >> start.sh && \
    echo 'wait' >> start.sh && \
    chmod +x start.sh

# Run all services
CMD ["./start.sh"]
