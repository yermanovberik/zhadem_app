FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk

WORKDIR /app

COPY ./gradle ./gradle
COPY ./gradlew ./
COPY build.gradle settings.gradle ./
RUN ./gradlew --version

COPY . .

RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim

COPY --from=build /app/build/libs/app.zhardem.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
