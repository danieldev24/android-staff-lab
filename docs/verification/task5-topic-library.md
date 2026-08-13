# Task 5 Verification — Topic Library Route

Date: 2026-08-13  
Device: Largest_Device Android Emulator, Android 16 / API 36, portrait

## Automated checks

The focused Compose test starts on the library, opens the stable Application Startup card, verifies the detail question, dispatches Android system back, and verifies the library is visible again.

```text
./gradlew :app:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.krahs.androidstafflab.navigation.AppNavigationTest \
  :app:assembleDebug :app:lintDebug

Starting 1 tests on Largest_Device(AVD) - 16
Finished 1 tests on Largest_Device(AVD) - 16
BUILD SUCCESSFUL
```

The TDD red run failed at the initial `Topic library` assertion before the route and screen existed. The same focused test passed after implementation.

## Runtime checks

- Performed a clean install and cleared app data before cold launch.
- Confirmed the initial destination exposes `Topic library` and one `Application startup` card.
- Tapped the card and confirmed the detail destination exposes `What happens when an Android application starts?`.
- Dispatched Android system back and confirmed `Topic library` is visible again.
- Checked `AndroidRuntime:E` and fatal log buffers after the flow; no entries were emitted.
- Repeated the library layout at 200% font scale. The card reflows vertically, remains scrollable, and the `OPEN TOPIC →` action stays readable.

## Artifacts

- `artifacts/task5-library-api36.png`
- `artifacts/task5-detail-api36.png`
- `artifacts/task5-library-api36-font200.png`
