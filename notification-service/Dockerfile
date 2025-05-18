FROM openjdk:21

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY pom.xml .
COPY target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5009 -jar app.jar"]
