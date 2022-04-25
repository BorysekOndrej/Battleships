# Battleships

This is a course project for [TDT4240 Software Architecture at NTNU](https://www.ntnu.edu/studies/courses/TDT4240) of Group 23 in Spring 2022. 


Android 8.0+ application written in Java using LibGDX and Firebase Cloud Messaging.
Server written in Java 1.8, Javalin as HTTPS server and Nginx as a reverse proxy.


## How to build

### Docker

Server dev:

```sh
docker-compose -f docker-compose.dev.yml up --build -d
```

Server prod:

```sh
docker-compose --compatibility up --build -d
```

App build (slow, but fully in docker):

```sh
./docker_build_apk.sh
```

App build (cachable, but requires local tooling):
```
./gradlew assembleDebug
```

### Security

For ease of development the file `android\google-services.json` is included in the repository. It contains API keys for Firebase, so it should idealy remain secret, however the keys are also recoverable from the APK, so it's not highly sensitive. Let's keep it in repository for now and revoke it before we make it public.


## Icon

Battleship by Phạm Thanh Lộc from NounProject.com
https://thenounproject.com/icon/battleship-2506976/
