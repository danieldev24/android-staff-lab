# Checkpoint A verification — Foundation

Verified: 2026-08-13  
Package: `com.krahs.androidstafflab`  
Device: `Largest_Device` AVD, Android 16, API 36, arm64  
Host JVM: Amazon Corretto 17.0.18

## Automated checks

| Check | Command | Result |
|---|---|---|
| Wrapper | `./gradlew --version` | Gradle 9.5.0, JDK 17 |
| Debug APK | `./gradlew :app:assembleDebug` | Pass |
| Android lint | `./gradlew :app:lintDebug` | Pass |
| Install | `./gradlew :app:installDebug` | Pass on API 36 emulator |
| APK contract | `aapt dump badging app-debug.apk` | package `com.krahs.androidstafflab`, min 26, target/compile 36 |

## Runtime checks

- Fresh app data followed by an explicit Activity launch reported `LaunchState: COLD` and completed without an `AndroidRuntime` error.
- App shell renders through edge-to-edge Compose content; the content remains vertically scrollable instead of assuming a fixed screen height.
- A 200% system font-scale run exposed a clipped decorative header marker. The marker was removed from the constrained row, a `fontScale = 2f` Preview guard was added, and the emulator run was repeated successfully.
- Returning from the launcher brings the existing task to the foreground without a crash. There is no persisted user state in this foundation slice.

## Visual evidence

- Default font scale: [foundation-api36.png](artifacts/foundation-api36.png)
- 200% font scale: [foundation-api36-font200.png](artifacts/foundation-api36-font200.png)

## Not applicable in Checkpoint A

- Navigation/back-stack and deep links begin in Task 5.
- Runtime permissions are not requested by the app.
- There is no network, offline mutation, long list or animation performance surface yet.

