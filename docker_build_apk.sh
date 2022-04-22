#!/bin/bash

docker build --tag battleships_apk -f DockerfileAndroid .
docker run --name battleships_apk_container battleships_apk /bin/true
docker cp battleships_apk_container:/app/Battleships_debug.apk ./build/Battleships_debug.apk
docker rm battleships_apk_container
