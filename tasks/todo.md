# Task Breakdown: Android Staff Lab — Application Startup

## Phase 1 — Runnable foundation

## Task 1: Pin Android toolchain contract

**Status:** Complete.

**Description:** Resolve the current official stable AGP/Kotlin/Compose/Navigation versions compatible with SDK 36, record the rationale, and establish a version catalog without introducing feature code.

- Acceptance: Exact versions are pinned; `minSdk=26`, `compileSdk=36`, `targetSdk=36`; only approved dependencies are declared.
- Verify: Inspect the catalog and run Gradle dependency resolution once the wrapper exists in Task 2.
- Dependencies: None.
- Files likely touched: `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `docs/sources/toolchain.md`.
- Estimated scope: Medium (4 files).

## Task 2: Add reproducible Gradle wrapper

**Status:** Complete.

**Description:** Add the wrapper files needed to run the pinned toolchain consistently from the command line and Android Studio.

- Acceptance: Wrapper distribution matches the chosen AGP compatibility range and contains no machine-local paths.
- Verify: `./gradlew --version` exits successfully.
- Dependencies: Task 1.
- Files likely touched: `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties`, `gradle/wrapper/gradle-wrapper.jar`.
- Estimated scope: Medium (4 generated files).

## Task 3: Build installable Compose shell

**Status:** Complete.

**Description:** Create the app module, manifest, package structure and minimal `MainActivity` so the project produces a debug APK before feature work begins.

- Acceptance: Application ID and namespace are `com.krahs.androidstafflab`; launcher activity renders a minimal Compose text; no startup-blocking work exists.
- Verify: `./gradlew :app:assembleDebug` passes and the APK contains the expected manifest package.
- Dependencies: Tasks 1–2.
- Files likely touched: `settings.gradle.kts`, `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`, `app/src/main/java/com/krahs/androidstafflab/MainActivity.kt`, `app/src/main/res/values/strings.xml`.
- Estimated scope: Medium (5 files).

## Task 4: Establish technical-trace app shell

**Status:** Complete.

**Description:** Replace the placeholder with the app root and a restrained Material 3 theme whose semantic lane colors can support the future timeline.

- Acceptance: App root uses Material 3; light/dark color roles meet readable contrast; no feature owns raw semantic colors.
- Verify: `./gradlew :app:assembleDebug` passes and Compose theme previews compile.
- Dependencies: Task 3.
- Files likely touched: `MainActivity.kt`, `AndroidStaffLabRoot.kt`, `ui/theme/Color.kt`, `ui/theme/Type.kt`, `ui/theme/Theme.kt`.
- Estimated scope: Medium (5 files).

## Checkpoint A: Foundation

**Status:** Complete. Evidence: `docs/verification/foundation.md`.

- Acceptance: Tasks 1–4 are complete and the debug app builds from a clean Gradle invocation.
- Verify: `./gradlew clean :app:assembleDebug`.

## Phase 2 — Core learning path

## Task 5: Deliver Topic Library route

**Description:** Add the two-destination navigation contract and make the single Application Startup topic discoverable from the library screen.

- Acceptance: Library lists one stable topic card; tapping it opens the correct detail destination; system back returns to Library.
- Verify: Run the focused Compose navigation test plus `./gradlew :app:assembleDebug`.
- Dependencies: Task 4.
- Files likely touched: `AndroidStaffLabRoot.kt`, `navigation/AppNavigation.kt`, `feature/library/Topic.kt`, `feature/library/TopicLibraryScreen.kt`, `androidTest/.../AppNavigationTest.kt`.
- Estimated scope: Medium (5 files).

## Task 6: Render cold-start timeline

**Description:** Deliver the first complete learning slice: a source-backed cold-start timeline with five lanes, ordered stages and staff-level detail cards.

- Acceptance: Cold mode renders at least five lanes and all spec stages; each selected stage exposes what/where/why/staff note; sources are represented by stable IDs.
- Verify: Run focused timeline UI tests and manually compare stage order against the approved spec.
- Dependencies: Task 5.
- Files likely touched: `feature/startup/content/StartupContent.kt`, `feature/startup/ui/ApplicationStartupScreen.kt`, `feature/startup/ui/StartupTimeline.kt`, `navigation/AppNavigation.kt`, `androidTest/.../StartupTimelineTest.kt`.
- Estimated scope: Medium (5 files).

## Task 7: Add startup flow player

**Description:** Extend the timeline into a deterministic Cold/Warm/Hot player driven by immutable state and explicit user actions.

- Acceptance: Mode selection, play/pause, previous, next and reset work; skipped stages are explicit; actions never exceed stage bounds.
- Verify: `./gradlew :app:testDebugUnitTest --tests '*StartupFlowViewModelTest*'` plus focused player UI test.
- Dependencies: Task 6.
- Files likely touched: `feature/startup/model/StartupFlowState.kt`, `feature/startup/StartupFlowViewModel.kt`, `feature/startup/ui/StartupFlowPlayer.kt`, `feature/startup/ui/ApplicationStartupScreen.kt`, `test/.../StartupFlowViewModelTest.kt`.
- Estimated scope: Medium (5 files).

## Task 8: Deliver critical-path simulation lab

**Description:** Add four deterministic workload toggles, TTID/TTFD calculation and a before/after optimization comparison without performing the simulated work.

- Acceptance: Four workloads affect the correct metric; “Apply staff-level fixes” produces deterministic before/after output; the non-benchmark label is always visible.
- Verify: Run simulation unit tests and Compose test that asserts TTID/TTFD plus the educational-model disclaimer.
- Dependencies: Task 7.
- Files likely touched: `feature/startup/model/StartupSimulation.kt`, `feature/startup/ui/StartupLab.kt`, `feature/startup/ui/ApplicationStartupScreen.kt`, `test/.../StartupSimulationTest.kt`, `androidTest/.../StartupLabTest.kt`.
- Estimated scope: Medium (5 files).

## Checkpoint B: Core learning

- Acceptance: Tasks 5–8 form an end-to-end library → topic → flow → lab journey with deterministic calculations.
- Verify: `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`.

## Phase 3 — Live showcase

## Task 9: Capture real startup events

**Description:** Instrument the app process with a constant-time recorder, educational provider and lifecycle hooks using monotonic relative timestamps.

- Acceptance: Provider, Application, activity lifecycle, Compose entry and first-frame events can produce at least seven ordered records; hooks perform no blocking I/O or sleep.
- Verify: Cold-launch through ADB, inspect the in-memory records, and run a focused ordering test where platform contracts permit assertions.
- Dependencies: Task 6; may be developed independently of Task 8 after Task 7 contracts stabilize.
- Files likely touched: `startuptrace/StartupEventRecorder.kt`, `startuptrace/StartupTraceProvider.kt`, `AndroidStaffLabApp.kt`, `MainActivity.kt`, `app/src/main/AndroidManifest.xml`.
- Estimated scope: Medium (5 files).

## Task 10: Present live startup event log

**Description:** Expose captured events on the topic screen with process/thread context, relative duration and an explicit observer-effect explanation.

- Acceptance: UI shows at least seven records after cold launch; timestamps are monotonic; wording limits claims to the observed run.
- Verify: Run the focused event-log Compose test and manually cold-launch twice via ADB.
- Dependencies: Task 9.
- Files likely touched: `feature/startup/ui/StartupEventLog.kt`, `feature/startup/ui/ApplicationStartupScreen.kt`, `feature/startup/StartupFlowViewModel.kt`, `test/.../StartupEventRecorderTest.kt`, `androidTest/.../StartupEventLogTest.kt`.
- Estimated scope: Medium (5 files).

## Task 11: Add staff notes source map

**Description:** Complete the staff-level caveats and provide a direct official-source path for every factual note shown in the app.

- Acceptance: Each factual note has an official Android/AOSP source ID and URL; USAP/provider/TTID caveats are worded without universal claims; links are accessible from UI.
- Verify: Run the source-map unit test and compare all URLs with `docs/sources/application-startup.md`.
- Dependencies: Tasks 6 and 10.
- Files likely touched: `docs/sources/application-startup.md`, `feature/startup/content/StartupContent.kt`, `feature/startup/ui/StaffNotes.kt`, `feature/startup/ui/SourceLinksSheet.kt`, `androidTest/.../StaffNotesTest.kt`.
- Estimated scope: Medium (5 files).

## Checkpoint C: Showcase

- Acceptance: Tasks 9–11 provide a truthful live showcase with complete official-source coverage.
- Verify: Cold-launch via ADB, then run `./gradlew :app:testDebugUnitTest :app:connectedDebugAndroidTest`.

## Phase 4 — Product quality

## Task 12: Verify accessible adaptive UI

**Description:** Harden interaction semantics, reduced-motion behavior, touch targets and layout reflow across the four dense visual components.

- Acceptance: All icon-only controls have action labels and >=48dp targets; reading order exposes lane/order/state; no content clips at 200% font scale.
- Verify: Run accessibility Compose tests, inspect TalkBack order, and manually test phone plus tablet width on API 36.
- Dependencies: Tasks 8, 10 and 11.
- Files likely touched: `feature/startup/ui/StartupTimeline.kt`, `feature/startup/ui/StartupFlowPlayer.kt`, `feature/startup/ui/StartupLab.kt`, `feature/startup/ui/StartupEventLog.kt`, `androidTest/.../StartupAccessibilityTest.kt`.
- Estimated scope: Medium (5 files).

## Task 13: Complete release-candidate gate

**Description:** Execute the complete verification matrix, document the runnable commands and fix only defects that block an approved success criterion.

- Acceptance: All nine spec criteria have evidence; clean build/unit/lint/instrumented checks pass; API 36 cold launch has no crash or intentional main-thread blocking.
- Verify: `./gradlew clean :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:connectedDebugAndroidTest` plus the documented ADB cold-launch script.
- Dependencies: Tasks 1–12.
- Files likely touched: `README.md`, `docs/verification/application-startup.md` (feature files only if a failing criterion requires a scoped fix).
- Estimated scope: Small (2 documentation files plus scoped fixes if required).

## Checkpoint D: Complete

- Acceptance: The approved Application Startup vertical slice is buildable, testable, accessible and source-backed.
- Verify: Review `docs/verification/application-startup.md` against the spec traceability table.
