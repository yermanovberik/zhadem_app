FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
LABEL authors="berikyerman"
RUN ./gradle bootJar --no-daemon
FROM openjdk:17-jdk-slim
COPY --from=build /build/libs/app.zhardem.jar app.jar
ENTRYPOINT ["java", "-java","app.jar"]
