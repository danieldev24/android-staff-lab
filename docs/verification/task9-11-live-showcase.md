# Tasks 9–11 — Live Startup Showcase Verification

Verified: 2026-08-13

## Scope

- Constant-time, in-memory startup recorder with provider, `Application`, Activity lifecycle, Compose-entry, and first-draw hooks.
- Observed-run event log with monotonic relative timestamps plus PID/thread context.
- Observer-effect warning and explicit boundary between the lab's draw observation and framework-reported TTID.
- Six staff caveats with stable source IDs and direct official Android/AOSP links.

## Automated evidence

Command:

```bash
ANDROID_SERIAL=emulator-5554 ./gradlew \
  :app:testDebugUnitTest \
  :app:lintDebug \
  :app:assembleDebug \
  :app:connectedDebugAndroidTest
```

Result:

- 15 JVM unit tests passed.
- 8 Android instrumentation/Compose tests passed.
- Debug lint passed.
- Debug APK assembled successfully.

Focused checks cover recorder uniqueness/monotonicity, provider → `Application` → Activity lifecycle ordering, seven rendered log records, observer wording, source-map domains, caveat wording, and source-link click semantics.

## Emulator evidence

- Device: `Largest_Device` AVD (`emulator-5554`)
- Android: 16 / API 36
- Font scale: 1.0
- `AndroidRuntime:E` log slice after final launch: empty

Two explicit launcher cold starts returned `LaunchState: COLD`, `Status: ok`:

- Run A: `TotalTime: 1144 ms`
- Run B: `TotalTime: 965 ms`

A final post-layout cold run also returned `LaunchState: COLD`, `Status: ok`. These ADB values are smoke evidence, not benchmark results.

The final in-app observation contained seven monotonic records:

1. `StartupTraceProvider.onCreate()`
2. `Application.onCreate()`
3. `MainActivity.onCreate()`
4. `MainActivity.onStart()`
5. `MainActivity.onResume()`
6. `Compose content entered`
7. `First frame observed`

## Visual evidence

- [Observed-run warning and first records](artifacts/task9-11-live-startup-api36.png)
- [All seven runtime records and staff-note entry](artifacts/task9-11-live-startup-events-api36.png)
- [Staff caveats and source actions](artifacts/task11-staff-notes-api36.png)

## Source-map comparison

`StartupContent.kt` and `docs/sources/application-startup.md` contain the same ten official URLs. The unit contract additionally rejects non-HTTPS URLs and hosts outside `developer.android.com` or `source.android.com`.
