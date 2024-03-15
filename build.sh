#!/bin/bash

apt-get update
apt-get install -y wget

./gradlew --version
./gradlew bootJar --no-daemon
