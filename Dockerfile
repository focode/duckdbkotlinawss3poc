# Use an official Ubuntu runtime as a parent image
FROM ubuntu:22.04

# Install necessary libraries for DuckDB
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    libc6 \
    libgcc1 \
    libstdc++6 \
    libcurl4 \
    zlib1g \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY ktor-sample-duckdb-0.0.1-SNAPSHOT.jar /app/ktor-sample-duckdb-0.0.1-SNAPSHOT.jar

# Set environment variables
ENV AWS_ACCESS_KEY_ID=somevalue
ENV AWS_SECRET_ACCESS_KEY=somevalue
ENV AWS_SESSION_TOKEN=somevalue

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "ktor-sample-duckdb-0.0.1-SNAPSHOT.jar"]


