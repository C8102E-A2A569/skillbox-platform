FROM openjdk:21

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY pom.xml .
COPY target/*.jar app.jar

# Set environment variables
ENV SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING=true

ENTRYPOINT ["sh", "-c", "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008 -jar app.jar"]
