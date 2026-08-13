# Task 8 Verification — Critical-path Simulation Lab

Date: 2026-08-13  
Device: Largest_Device Android Emulator, Android 16 / API 36, portrait

## Delivered behavior

- Four independently toggleable educational workloads:
  - SDK auto-init in `ContentProvider`;
  - disk read in `Application.onCreate()`;
  - heavy initial composition;
  - deferred required data after first frame.
- `Run simulation` calculates deterministic TTID/TTFD without running, sleeping, reading, decoding, or blocking.
- Blocking workloads increase both TTID and TTFD; deferred required data increases TTFD only.
- `Apply staff-level fixes` produces a non-mutating before/after comparison.
- `Educational model — not a device benchmark` is always visible in the lab header.

## Deterministic model contract

| Scenario | TTID | TTFD |
| --- | ---: | ---: |
| Baseline | 320 ms | 480 ms |
| All workloads enabled | 680 ms | 1,060 ms |
| All workloads after staff fixes | 360 ms | 760 ms |

The values are deliberately fixed teaching inputs, not measurements. TTID and TTFD terminology follows:

- https://developer.android.com/topic/performance/vitals/launch-time

## TDD evidence

The first unit run failed because the simulation contract did not exist. The first focused UI run failed because `Critical-path lab` did not exist. After implementation:

```text
./gradlew :app:testDebugUnitTest --tests '*StartupSimulationTest*'
BUILD SUCCESSFUL

./gradlew :app:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.krahs.androidstafflab.feature.startup.ui.StartupLabTest

Starting 1 tests on Largest_Device(AVD) - 16
Finished 1 tests on Largest_Device(AVD) - 16
BUILD SUCCESSFUL
```

Five unit tests cover baseline values, three blocking paths, deferred-only TTFD impact, reversible toggles, and deterministic before/after output. The Compose test covers four workload controls, disclaimer, explicit run, live metrics, and comparison output.

## Combined gate

```text
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:connectedDebugAndroidTest

Starting 5 tests on Largest_Device(AVD) - 16
Largest_Device(AVD) - 16 Tests 5/5 completed. (0 skipped) (0 failed)
BUILD SUCCESSFUL
```

The unit suite contains ten Task 7–8 tests. Android crash output remained empty during the manual API 36 flow.

## Artifacts

- `artifacts/task8-critical-path-lab-overview-api36.png`
- `artifacts/task8-critical-path-lab-api36.png`
