FROM openjdk:21-slim

LABEL authors="herasimovich-v"

WORKDIR /app

COPY target/app.jar app.jar
COPY wait-for-it.sh /app/wait-for-it.sh

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "./wait-for-it.sh record_service_db:5433 -- java -jar app.jar"]