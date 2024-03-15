#!/bin/bash

docker compose up --build
docker run -d -p 9000:3421 minio/minio:latest
docker run -d -p 8080:8080 appzhardem-app:latest

