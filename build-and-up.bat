@echo off
call gradlew clean build
docker build -t library_backend .
docker compose up
docker compose up