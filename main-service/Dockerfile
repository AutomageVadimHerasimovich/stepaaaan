FROM openjdk:21-slim

LABEL authors="herasimovich-v"

WORKDIR /app

COPY target/library-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /app/wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "./wait-for-it.sh library_db:5432 -- java -jar app.jar"]