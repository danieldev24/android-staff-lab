# Task 12 — Accessible Adaptive UI Verification

Verified: 2026-08-13

## Scope

- Explicit semantics for timeline stage order/lane/state, workload switch state, and observed-event order/process/thread/time.
- Minimum 48 dp targets for every dense player and lab action. The current UI has no icon-only action.
- Polite live regions for the current player stage and calculated result cards.
- Reduced-motion behavior that replaces autoplay with an explicit one-stage `Step` action.
- Large-text reflow and a centered readable-width bound for wide displays.
- Safe drawing insets outside the scroll viewport, so content cannot scroll underneath system bars.

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
- 11 Android instrumentation/Compose tests passed.
- Debug lint passed.
- Debug APK assembled successfully.

`StartupAccessibilityTest` checks all seven dense action targets at 48 dp or greater, plus click semantics. It also asserts the first stage's ordinal, lane and current state; workload `Role.Switch`/off state; and a single merged event description with ordinal, PID, thread and relative time.

`StartupReducedMotionTest` checks that the primary motion control is labeled `Step` and emits `Next`, not `PlayPause`, when reduced motion is active.

## Manual API 36 matrix

- Device: `Largest_Device` AVD (`emulator-5554`), Android 16 / API 36.
- Phone: 412 dp effective width, font scale 2.0. Mode chips, player actions, metrics and event metadata reflow vertically; no horizontal clipping was observed.
- Tablet: 900 dp effective width (`1800x2560`, density 320), font scale 1.0. The complete flow player and five-lane map fit without clipping; topic content remains centered and bounded at 960 dp.
- Reduced motion: system animator duration scale `0`; the app exposed `Step` and the explanatory reduced-motion note.
- TalkBack: the real TalkBack service was enabled and bound on API 36. Forward focus started at the header mark and followed the visual route into the topic; the critical stage/workload/event utterance contracts are additionally fixed by Compose semantics assertions.
- Emulator font, density, display size, animator scale and accessibility-service settings were restored after verification.

## Visual evidence

- [Phone — flow player at 200% font](artifacts/task12-phone-font200-player.png)
- [Phone — simulation lab at 200% font](artifacts/task12-phone-font200-lab.png)
- [Phone — event records at 200% font](artifacts/task12-phone-font200-events.png)
- [Tablet — topic and flow player](artifacts/task12-tablet-api36-detail.png)
- [Reduced-motion `Step` mode](artifacts/task12-reduced-motion-api36.png)
- [TalkBack focus ring on API 36](artifacts/task12-talkback-order-api36.png)
