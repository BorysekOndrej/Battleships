# Battleships

Requires platform Android 10 (api 29). On Android studio, go to `Tools > SDK Manager`
and install the required version.

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

