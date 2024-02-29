FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY build.gradle settings.gradle ./
RUN ./gradlew --version
COPY . .
RUN ./gradlew bootJar --no-daemon
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/app.zhardem-1.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]