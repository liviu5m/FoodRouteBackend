# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
# THE FIX: Use the correct tag '17-jre' instead of '17-jdk-jre'
FROM eclipse-temurin:17-jreap

# Create a non-root user for security best practices
RUN useradd --create-home --shell /bin/bash appuser
USER appuser

# Copy the JAR from the build stage
COPY --from=build /target/foodroute-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]