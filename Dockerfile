FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY build.gradle settings.gradle ./
RUN ./gradlew --version
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y wget
WORKDIR /minio
RUN wget https://dl.min.io/server/minio/release/linux-amd64/minio && \
    chmod +x minio
COPY ./minio-start.sh .
RUN chmod +x minio-start.sh
VOLUME /minio/data
EXPOSE 9000
ENTRYPOINT ["./minio-start.sh"]

COPY --from=build /app/build/libs/app.zhardem-1.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
