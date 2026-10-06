# Private Chat Android

Kotlin and Jetpack Compose Android client foundation for the private two-person chat.

Open this directory in Android Studio with JDK 17 and Android SDK 35. The debug emulator API default is `http://10.0.2.2:8000/`; override it with `-PapiBaseUrl=https://your-api/`. Cleartext traffic is allowed only in debug builds. Release builds require HTTPS.

The initial screen signs in against `POST /api/auth/token/` and stores access/refresh tokens in encrypted preferences backed by Android Keystore. No account registration flow is present. Firebase configuration and signing keys are intentionally excluded from the repository.

This phase provides the login UI and client foundation. Conversation UI, WebSockets, push notifications, and image sharing are later phases.
