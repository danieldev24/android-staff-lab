# Application Startup — Official Source Map

Verified: 2026-08-13

Tasks 6–11 use stable source IDs in `StartupContent.kt`. These IDs are the contract between learning copy, staff notes, runtime-observation caveats, and the source-link UI.

| Stable ID | Official source | Claims used in the cold-start timeline |
| --- | --- | --- |
| `android-app-startup-time` | [App startup time](https://developer.android.com/topic/performance/vitals/launch-time) | Cold start creates the process/app/main activity/UI; a starting window can remain until first draw; TTID is first frame, while TTFD includes asynchronously loaded content required for usability and requires a fully-drawn signal. |
| `aosp-zygote` | [About the Zygote processes](https://source.android.com/docs/core/runtime/zygote) | Zygote can fork on demand or system_server can specialize a pooled USAP; the USAP pool is conditional on platform configuration. |
| `android-processes-and-threads` | [Processes and threads overview](https://developer.android.com/guide/components/processes-and-threads) | Android starts a Linux process when no application component is running; components default to one process/main thread; blocking that thread blocks callbacks and drawing. |
| `android-application-on-create` | [`Application.onCreate()`](https://developer.android.com/reference/android/app/Application#onCreate()) | The callback runs before Activity, Service, and Receiver objects, excluding ContentProvider; implementations must be fast because their time directly affects startup of the first component in that process. |
| `android-content-provider` | [`ContentProvider`](https://developer.android.com/reference/android/content/ContentProvider) | `ContentProvider.onCreate()` is called on the application main thread and should avoid lengthy work. |
| `android-activity-lifecycle` | [The activity lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle) | The launch Activity moves through `onCreate()` → `onStart()` → `onResume()` in sequence. |
| `compose-phases` | [Jetpack Compose phases](https://developer.android.com/develop/ui/compose/phases) | Compose produces UI through composition → layout → drawing. |
| `android-app-startup` | [App Startup](https://developer.android.com/topic/libraries/app-startup) | Multiple ContentProviders have undetermined initialization order; App Startup shares one provider and expresses dependency order explicitly. |
| `android-system-clock` | [`SystemClock`](https://developer.android.com/reference/android/os/SystemClock) | `elapsedRealtimeNanos()` is monotonic and suitable for general-purpose elapsed interval observations. |
| `android-view-tree-observer` | [`ViewTreeObserver.OnDrawListener`](https://developer.android.com/reference/android/view/ViewTreeObserver.OnDrawListener) | The callback observes when the view tree is about to be drawn; the lab labels this as an observed frame rather than framework-reported TTID. |

## Claim boundaries

- The timeline teaches the launcher Activity path, but explicitly treats launcher, deep link, notification, provider, service, and receiver as different possible entry points.
- USAP is shown as optional, not a universal device step.
- Provider creation before `Application.onCreate()` is a lifecycle boundary; no ordering is claimed between independent providers.
- TTID is not described as “fully usable.” TTFD depends on the app reporting the appropriate fully-drawn state.
- The timeline is educational content. It does not contain benchmark numbers or simulate blocking work.
- The live recorder is also educational instrumentation. It adds observer cost, stores only in memory, and does not claim that its first-draw callback is the Android Framework TTID metric.

## Staff-note coverage

Every factual staff note in the UI owns at least one source ID. The source sheet resolves those IDs from the same `StartupContent.sources` map and exposes the exact official URL:

- Entry-point variability → `android-app-startup-time`
- Conditional USAP path → `aosp-zygote`
- Partial provider ordering → `android-application-on-create`, `android-content-provider`, `android-app-startup`
- Per-process `Application` mental model → `android-processes-and-threads`, `android-application-on-create`
- TTID versus TTFD → `android-app-startup-time`
- Observed draw versus framework metric → `android-system-clock`, `android-view-tree-observer`, `android-app-startup-time`
