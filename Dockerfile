FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY build.gradle settings.gradle ./
RUN ./gradlew --version
COPY . .
RUN ./gradlew bootJar --no-daemon

