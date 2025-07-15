FROM openjdk:21-slim

LABEL authors="herasimovich-v"

WORKDIR /app

COPY build/libs/task1-0.0.1-SNAPSHOT.jar app.jar
COPY docker-compose.yml /app/docker-compose.yml
COPY wait-for-it.sh /app/wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "./wait-for-it.sh library_db:5432 -- java -jar app.jar"]