# Task 7 Verification — Startup Flow Player

Date: 2026-08-13  
Device: Largest_Device Android Emulator, Android 16 / API 36, portrait

## Delivered behavior

- Cold, Warm, and Hot modes share the nine-stage timeline.
- Cold keeps all stages active.
- Warm reuses the app process and explicitly skips Zygote/process/Application/provider creation while retaining launch handoff, Activity, Compose, and first-frame stages.
- Hot retains launch/system handoff and first-frame visibility while marking process, Activity, and initial Compose creation stages skipped.
- Play/pause, previous, next, reset, direct stage selection, and mode selection are explicit actions against immutable `StartupFlowUiState`.
- Previous/next/playback traverse active stages only and cannot move beyond the first or final active stage.
- Timeline semantics expose current, pending, and `Skipped in <mode>` states.
- Playback timing uses a cancellable Compose `LaunchedEffect`; no thread is blocked.

The state owner follows Android's current ViewModel/StateFlow guidance and the screen collects it using `collectAsStateWithLifecycle()`:

- https://developer.android.com/develop/ui/compose/state-hoisting
- https://developer.android.com/develop/ui/compose/state#other-supported-types-of-state
- https://developer.android.com/topic/libraries/architecture/coroutines

## TDD evidence

The first unit run failed because the flow model and ViewModel did not exist. The first focused UI run failed because `Startup flow player` did not exist. After implementation:

```text
./gradlew :app:testDebugUnitTest --tests '*StartupFlowViewModelTest*'
BUILD SUCCESSFUL

./gradlew :app:connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.krahs.androidstafflab.feature.startup.ui.StartupFlowPlayerTest

Starting 1 tests on Largest_Device(AVD) - 16
Finished 1 tests on Largest_Device(AVD) - 16
BUILD SUCCESSFUL
```

Five unit tests cover mode reset, Warm/Hot skip contracts, active-stage traversal, bounds, play completion, and reset. The Compose test covers Warm selection, skipped semantics, previous/next/reset, and play/pause UI state.

## Runtime checks

- Verified the complete player and stage rail at 100% font scale.
- Verified 200% font-scale reflow; all content remains vertically scrollable and controls remain reachable.
- Android crash log remained empty during the checked flow.

## Artifacts

- `artifacts/task7-flow-player-api36.png`
- `artifacts/task7-flow-player-api36-font200.png`
