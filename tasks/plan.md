# Implementation Plan: Android Staff Lab — Application Startup

Status: Complete — Checkpoint D verified  
Source spec: `specs/application-startup.md`  
Package: `com.krahs.androidstafflab`

## Overview

Xây một greenfield Jetpack Compose app theo các vertical slice có thể build và kiểm chứng độc lập. Slice đầu đi từ runnable shell đến Topic Library, cold-start timeline, interactive Cold/Warm/Hot player, critical-path lab, live startup trace, sau đó mới làm source notes, accessibility và emulator verification. Không thêm benchmark module, DI, database, network hoặc chart dependency trong phạm vi này.

## Architecture Decisions

- **Single app module trước:** đủ cho một learning topic; chỉ modularize khi có bằng chứng build/ownership cần thiết.
- **Single activity + type-safe Navigation Compose:** Library và Topic Detail là hai destination đầu tiên; contract navigation được chốt trước UI feature.
- **Immutable domain model + ViewModel/UDF:** timeline và simulation calculation tách khỏi composable để deterministic unit test.
- **Compose primitives/Canvas:** timeline và bar chart không cần third-party chart dependency.
- **Hybrid showcase:** system-side flow là source-backed simulation; app-side callback order dùng live recorder với monotonic timestamp.
- **Simulation không block thật:** mọi workload cost là data model; production/debug startup path không có `sleep`, disk I/O hay network I/O có chủ đích.
- **Reusable education design system:** visual direction is captured in `docs/design-system/visual-language.md`; features consume semantic tokens and shared Compose primitives rather than raw colors or one-off shapes.
- **Accessibility là constraint kiến trúc:** semantic state, 48dp targets, reduced motion và 200% font scale được thiết kế cùng component contract.

## Dependency Graph

```text
Toolchain contract
    └── Gradle wrapper
          └── Runnable Compose shell
                └── App theme/root
                      └── Topic Library + navigation
                            └── Cold-start timeline
                                  ├── Cold/Warm/Hot flow player
                                  │     └── Critical-path simulation lab
                                  └── Live startup event capture
                                        └── Live event-log UI
                                              ├── Source-backed staff notes
                                              └── Accessibility/adaptive pass
                                                    └── Final quality gate
```

## Task Order

### Phase 1 — Runnable foundation

1. Pin stable Android toolchain contract.
2. Add reproducible Gradle wrapper.
3. Produce the smallest installable Compose app.
4. Establish technical-trace theme and app root.

### Checkpoint A — Foundation

- `./gradlew :app:assembleDebug` passes.
- Package/application ID is `com.krahs.androidstafflab` everywhere.
- No dependency exists outside the approved minimal stack.

### Phase 2 — Core learning path

5. Deliver Topic Library navigation slice.
6. Deliver source-backed cold-start timeline slice.
7. Add Cold/Warm/Hot interactive flow player.
8. Add deterministic critical-path simulation lab.

### Checkpoint B — Core learning

- A user can open the topic and complete the full visual flow.
- Cold/Warm/Hot mode behavior and TTID/TTFD calculations have deterministic tests.
- Simulation is visibly labeled as educational, not benchmark data.

### Phase 3 — Live showcase

9. Capture real process startup events.
10. Present the live startup event log.
11. Add source-backed staff notes and citations.

### Checkpoint C — Showcase

- A cold launch records at least seven monotonic events from provider to first frame.
- UI explains observer effect and does not imply universal provider ordering.
- Every factual staff note maps to an official source.

### Phase 4 — Product quality

12. Complete accessibility and adaptive-layout verification.
13. Run final build, tests, lint and emulator quality gate.

### Checkpoint D — Complete

- All nine spec success criteria are evidenced.
- App launches on API 36 without crash and works at 200% font scale.
- No intentional main-thread blocking exists in the startup path.

## Success-Criteria Traceability

| Spec criterion | Covered by tasks |
|---|---|
| SC1 Build, unit tests, lint pass | 3, 13 |
| SC2 Two destinations work | 5, 13 |
| SC3 Three modes, five lanes | 6, 7 |
| SC4 Player actions work | 7 |
| SC5 Four workload toggles, TTID/TTFD, comparison | 8 |
| SC6 Seven monotonic live events | 9, 10 |
| SC7 Accessibility and 200% font scale | 12, 13 |
| SC8 Official sources for staff notes | 6, 11 |
| SC9 No startup-path blocking I/O/sleep | 9, 13 |

## Verification Strategy

- Run the smallest task-specific unit/UI test first.
- Run `:app:assembleDebug` at every checkpoint.
- Run `:app:testDebugUnitTest` and `:app:lintDebug` from Checkpoint B onward.
- Run `:app:connectedDebugAndroidTest` and manual font-scale checks only after the UI contract stabilizes.
- Treat Compose Preview as design feedback only; emulator evidence is required for completion.

## Risks and Mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| Local Android Studio/toolchain is newer than cached dependencies | High | Pin versions from the current official stable template; verify dependency resolution before feature code |
| “First frame observed” is confused with TTFD | High | Model TTID/TTFD separately and explain the signal in UI/source notes |
| Educational provider changes the startup it observes | Medium | Keep provider work constant-time and disclose observer effect |
| Provider order is presented as guaranteed | High | Assert only provider-before-`Application.onCreate()` for the recorded provider; state inter-provider order is undefined |
| Animation makes UI tests flaky | Medium | Expose deterministic previous/next/reset paths and disable/advance test clock |
| Dense timeline clips at large font scale | Medium | Reflow lanes/cards vertically and verify at 200% before final checkpoint |
| System implementation varies by device/version | Medium | Label Zygote/USAP details as AOSP implementation and optional where appropriate |

## Parallelization Notes

- After Task 7 fixes the state contract, Task 8 simulation work and Task 9 event capture are logically independent.
- Task 11 source notes can proceed after Task 6 content IDs stabilize.
- Shared files `ApplicationStartupScreen.kt`, `MainActivity.kt` and `AndroidManifest.xml` make their owning tasks sequential integration points.
- This plan does not authorize sub-agent execution; it only records safe dependency boundaries.

## Open Questions

None. Exact stable dependency versions are a Task 1 output, not a product decision.
