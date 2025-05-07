FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY pom.xml .
COPY target/*.jar app.jar

# Set environment variables
ENV SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING=true

CMD ["java", "-jar", "app.jar"]
