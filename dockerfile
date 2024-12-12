# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/CurrencyRates-0.0.1-SNAPSHOT.jar app.jar

# Copy application properties
COPY src/main/resources/application.properties application.properties

# Expose the application port
EXPOSE 8094

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/currency_rates
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

# Use the application.properties file
ENTRYPOINT ["java", "-jar", "app.jar"]