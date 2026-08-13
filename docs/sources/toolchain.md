# Android Staff Lab toolchain contract

Verified: 2026-08-13  
Target: Android SDK 36, minimum SDK 26

## Pinned versions

| Tool or library | Version | Rationale |
|---|---:|---|
| Android Gradle Plugin | 9.3.1 | Current stable patch in Google's Maven metadata; supports API 36 and Android Studio Quail 3 |
| Gradle | 9.5.0 | Required/default version for AGP 9.3 |
| JDK | 17 | AGP 9.3 documented default; matches the command-line JDK installed locally |
| Kotlin / Compose compiler plugin | 2.3.21 | Version documented with AGP 9.3 and the Compose compiler setup |
| Compose BOM | 2026.06.01 | Maps core Compose artifacts to 1.11.4, the latest BOM line compatible with approved `compileSdk = 36` |
| Activity Compose | 1.13.0 | Current stable Activity release |
| Navigation Compose | 2.9.8 | Current stable Navigation 2 release with type-safe destinations |
| Lifecycle ViewModel Compose | 2.10.0 | Stable version used by the SDK-36-compatible Compose setup |

The newer Compose BOM `2026.08.00` maps core Compose artifacts to 1.12.0. Official Compose setup documentation states that Compose 1.12.0 requires `compileSdk = 37`, so it is intentionally not used while this project targets SDK 36.

AGP 9 enables built-in Kotlin. The project therefore does not apply `org.jetbrains.kotlin.android`; only the Compose compiler and serialization plugins are versioned separately where those capabilities are needed.

## Official sources

- AGP/Gradle/JDK/API compatibility: https://developer.android.com/build/releases/agp-9-3-0-release-notes
- AGP and Android Studio compatibility: https://developer.android.com/build/releases/about-agp
- Built-in Kotlin migration contract: https://developer.android.com/build/migrate-to-built-in-kotlin
- Compose compiler and BOM setup: https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler
- Compose BOM mapping: https://developer.android.com/develop/ui/compose/bom/bom-mapping
- Activity releases: https://developer.android.com/jetpack/androidx/releases/activity
- Navigation releases: https://developer.android.com/jetpack/androidx/releases/navigation
- Lifecycle releases: https://developer.android.com/jetpack/androidx/releases/lifecycle

Repository metadata used to confirm stable patch availability:

- https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/maven-metadata.xml
- https://dl.google.com/dl/android/maven2/androidx/compose/compose-bom/maven-metadata.xml

