# Android Staff Lab

Android Staff Lab is a native Kotlin/Jetpack Compose learning app for building staff-level Android mental models through visual flows, deterministic simulations, and source-backed runtime showcases.

The first complete topic answers:

> **What happens when an Android application starts?**

It teaches the topic as four focused lessons—Overview, Flow, Lab, and Evidence—with a five-lane startup trace, Cold/Warm/Hot player, TTID/TTFD critical-path lab, live in-process event log, and official Android/AOSP source notes.

## Project contract

- Package/application ID: `com.krahs.androidstafflab`
- `minSdk = 26`, `compileSdk = 36`, `targetSdk = 36`
- JDK 17, Gradle wrapper 9.5.0, Android Gradle Plugin 9.3.1
- Single activity, Jetpack Compose Material 3, type-safe Navigation Compose
- No network, database, DI, image-loader, or third-party chart dependency
- Simulation costs are educational data, never device benchmark claims

Exact dependency versions and their official sources are recorded in [docs/sources/toolchain.md](docs/sources/toolchain.md).

## Build and verify

Prerequisites:

1. JDK 17.
2. Android SDK Platform 36 plus a configured `local.properties` or `ANDROID_HOME`.
3. An API 36 emulator for instrumentation and manual checks.

Run the complete release-candidate gate:

```bash
ANDROID_SERIAL=emulator-5554 ./gradlew clean \
  :app:assembleDebug \
  :app:testDebugUnitTest \
  :app:lintDebug \
  :app:connectedDebugAndroidTest
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Run the app

```bash
ANDROID_SERIAL=emulator-5554 ./gradlew :app:installDebug
adb -s emulator-5554 shell am start -W \
  -n com.krahs.androidstafflab/.MainActivity
```

Open the **Android Platform** category, then choose **What happens when an Android application starts?** System back returns to the library.

## Reproduce the cold-launch smoke check

This verifies launch status and crashes; the timing output is not treated as a benchmark:

```bash
adb -s emulator-5554 shell pm clear com.krahs.androidstafflab
adb -s emulator-5554 logcat -c
adb -s emulator-5554 shell am start -S -W \
  -n com.krahs.androidstafflab/.MainActivity
adb -s emulator-5554 logcat -d -s AndroidRuntime:E '*:S'
```

Expected result: `Status: ok`, `LaunchState: COLD`, and no `AndroidRuntime:E` crash entry.

## Structure

```text
app/src/main/java/com/krahs/androidstafflab/
  startuptrace/          constant-time in-memory launch hooks
  navigation/            Library and topic destinations
  feature/library/       discoverable topic entry
  feature/startup/       content, state, visual flow, lab and event log
  ui/designsystem/       reusable learning-app components/tokens
  ui/theme/              Material theme and semantic colors
app/src/test/            deterministic domain contracts
app/src/androidTest/     navigation, interaction and accessibility contracts
docs/sources/            official source maps
docs/verification/       emulator screenshots and task evidence
```

## Evidence

- [Final Application Startup verification](docs/verification/application-startup.md)
- [Official Application Startup source map](docs/sources/application-startup.md)
- [Accessible/adaptive UI matrix](docs/verification/task12-accessible-adaptive-ui.md)
- [Education redesign verification](docs/verification/education-redesign.md)
- [Design language](docs/design-system/visual-language.md)
- [Education learning flow](docs/design-system/education-learning-flow.md)
