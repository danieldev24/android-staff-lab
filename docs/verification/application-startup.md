# Application Startup — Release Candidate Verification

Verified: 2026-08-13  
Package: `com.krahs.androidstafflab`  
Device: `Largest_Device` AVD (`emulator-5554`), Android 16 / API 36

## Release-candidate gate

Command:

```bash
ANDROID_SERIAL=emulator-5554 ./gradlew clean \
  :app:assembleDebug \
  :app:testDebugUnitTest \
  :app:lintDebug \
  :app:connectedDebugAndroidTest
```

Result: `BUILD SUCCESSFUL`; all 83 actions executed after `clean`.

- 15 JVM unit tests: passed, 0 failures.
- 11 Android instrumentation/Compose tests: passed, 0 failures.
- Debug lint: passed.
- Debug APK: assembled successfully.

## API 36 cold-launch smoke check

Commands:

```bash
ANDROID_SERIAL=emulator-5554 ./gradlew :app:installDebug
adb -s emulator-5554 shell pm clear com.krahs.androidstafflab
adb -s emulator-5554 logcat -c
adb -s emulator-5554 shell am start -S -W \
  -n com.krahs.androidstafflab/.MainActivity
adb -s emulator-5554 logcat -d -s AndroidRuntime:E '*:S'
```

Observed result:

```text
Status: ok
LaunchState: COLD
Activity: com.krahs.androidstafflab/.MainActivity
TotalTime: 592
WaitTime: 592
Complete
```

The app process remained alive and the filtered `AndroidRuntime:E` output was empty. Timing is retained only as smoke evidence, not a benchmark result.

A production-source scan returned no `Thread.sleep`, `SystemClock.sleep`, `runBlocking`, file-read/write, socket, URL connection, or OkHttp call. Startup hooks only append constant-size records to the in-memory recorder; the app does not intentionally perform main-thread disk/network I/O or sleep.

## Success-criteria traceability

| Criterion | Evidence | Result |
| --- | --- | --- |
| SC1 — Build, unit tests and lint | Clean release gate above; [foundation](foundation.md) | Pass |
| SC2 — Exactly two working destinations | `AppNavigationTest`; [Task 5](task5-topic-library.md) | Pass |
| SC3 — Three modes and at least five lanes | `StartupTimelineTest`, `StartupFlowPlayerTest`; [Tasks 6–7](task6-cold-start-timeline.md) | Pass |
| SC4 — Player actions | Five ViewModel tests plus player Compose test; [Task 7](task7-startup-flow-player.md) | Pass |
| SC5 — Four workloads, TTID/TTFD, comparison and disclaimer | Five simulation tests plus lab Compose test; [Task 8](task8-critical-path-lab.md) | Pass |
| SC6 — Seven monotonic observed events | Recorder unit/instrumentation tests and event-log Compose test; [Tasks 9–11](task9-11-live-showcase.md) | Pass |
| SC7 — Accessibility and font scale 200% | Semantics/touch-target/reduced-motion tests plus phone, tablet and TalkBack matrix; [Task 12](task12-accessible-adaptive-ui.md) | Pass |
| SC8 — Official source for each factual staff note | Source-map unit test and ten-URL source map; [official source map](../sources/application-startup.md) | Pass |
| SC9 — No intentional startup-path blocking | Static production-source scan and API 36 cold launch above | Pass |

## Manual product matrix

- Library → Application Startup → system back: verified on API 36 and covered by instrumentation.
- Phone at 200% font: verified across the flow player, lane timeline, critical-path lab and live event log without horizontal clipping.
- Tablet width: verified at 900 dp effective width; content remains centered with a readable 960 dp maximum.
- Reduced motion: animator scale `0` exposes deterministic `Step` behavior instead of autoplay.
- TalkBack: service enabled and bound for focus-order inspection; lane/order/state and event utterances are fixed by semantics tests.
- Emulator font, density, display size, animation and accessibility-service settings were restored after verification.

## Final boundary

The Application Startup vertical slice meets all nine approved criteria. Its simulation and ADB timing are educational/smoke artifacts, not performance benchmark claims. Macrobenchmark and Baseline Profiles remain intentionally outside this slice.
