FROM openjdk:21-slim

LABEL authors="herasimovich-v"

WORKDIR /app

COPY build/libs/task1-0.0.1-SNAPSHOT.jar app.jar
COPY docker-compose.yml /app/docker-compose.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]