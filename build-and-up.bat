@echo off
call mvnw clean package -DskipTests
docker compose up --build --force-recreate