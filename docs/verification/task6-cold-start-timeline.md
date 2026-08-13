# Task 6 Verification — Cold-start Timeline

Date: 2026-08-13  
Device: Largest_Device Android Emulator, Android 16 / API 36, portrait

## Delivered learning slice

- Five semantic lanes: User / Launcher, `system_server`, Zygote / optional USAP, App process · main thread, and Render pipeline.
- Nine ordered UI stages. The first approved spec stage is split into launch request and system resolve/schedule so the User and system_server boundaries stay visible; approved stages 2–8 then map in order to UI stages 3–9.
- One expanded stage at a time, with `What happens`, `Where it runs`, `Why it matters`, `Staff note`, and stable official source IDs.
- Immutable content models and a stateless timeline; only the current selection is local saveable UI state. Task 7 can replace this owner with flow-player state without rewriting the timeline.

## Source verification

The content was checked against Android Developers and AOSP on 2026-08-13. The source-ID contract and claim boundaries are recorded in `docs/sources/application-startup.md`.

The implementation validates at runtime that stage order is contiguous, IDs are unique, and every factual stage source ID resolves to the source map.

## TDD evidence

The first focused run failed both tests because `Cold-start timeline` did not exist. After implementation, the tests verify:

- exactly five lane semantics;
- all nine stable stage IDs in approved order;
- provider-stage selection semantics;
- the four learning layers;
- stable Application, ContentProvider, and App Startup source IDs.

```text
./gradlew :app:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.krahs.androidstafflab.feature.startup.ui.StartupTimelineTest

Starting 2 tests on Largest_Device(AVD) - 16
Finished 2 tests on Largest_Device(AVD) - 16
BUILD SUCCESSFUL
```

## Full gate

```text
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:connectedDebugAndroidTest

Starting 3 tests on Largest_Device(AVD) - 16
Finished 3 tests on Largest_Device(AVD) - 16
BUILD SUCCESSFUL
```

`testDebugUnitTest` reports `NO-SOURCE`; Task 6 behavior is covered by focused instrumented Compose tests as planned.

## Runtime checks

- Installed the debug APK and cold-launched after clearing app data.
- Android crash buffer remained empty after launch.
- Compared stage order manually with the approved spec mapping above.
- At 100% font scale, the hero, lane map, and first selected stage render without horizontal clipping.
- At 200% font scale, header, hero, lane map, stage rail, and expanded learning copy reflow vertically and remain scrollable.

## Artifacts

- `artifacts/task6-timeline-api36.png`
- `artifacts/task6-timeline-api36-font200.png`
- `artifacts/task6-timeline-api36-font200-scrolled.png`
