package com.krahs.androidstafflab.feature.startup.content

import androidx.compose.runtime.Immutable

@Immutable
enum class StartupLane(
    val label: String,
    val symbol: String,
) {
    USER_LAUNCHER("User / Launcher", "U"),
    SYSTEM_SERVER("system_server", "S"),
    ZYGOTE_USAP("Zygote / optional USAP", "Z"),
    APP_MAIN_THREAD("App process · main thread", "A"),
    RENDER_PIPELINE("Render pipeline", "R"),
}

@Immutable
data class StartupSource(
    val id: String,
    val title: String,
    val url: String,
)

@Immutable
data class StartupStage(
    val id: String,
    val order: Int,
    val lane: StartupLane,
    val title: String,
    val summary: String,
    val whatHappens: String,
    val whereItRuns: String,
    val whyItMatters: String,
    val staffNote: String,
    val sourceIds: List<String>,
)

@Immutable
data class StaffNote(
    val id: String,
    val title: String,
    val body: String,
    val sourceIds: List<String>,
)

object StartupSourceIds {
    const val APP_STARTUP_TIME = "android-app-startup-time"
    const val ZYGOTE = "aosp-zygote"
    const val PROCESSES_AND_THREADS = "android-processes-and-threads"
    const val APPLICATION_ON_CREATE = "android-application-on-create"
    const val CONTENT_PROVIDER = "android-content-provider"
    const val ACTIVITY_LIFECYCLE = "android-activity-lifecycle"
    const val COMPOSE_PHASES = "compose-phases"
    const val APP_STARTUP_LIBRARY = "android-app-startup"
    const val SYSTEM_CLOCK = "android-system-clock"
    const val VIEW_TREE_OBSERVER = "android-view-tree-observer"
}

object StartupContent {
    val sources: Map<String, StartupSource> = listOf(
        StartupSource(
            id = StartupSourceIds.APP_STARTUP_TIME,
            title = "App startup time",
            url = "https://developer.android.com/topic/performance/vitals/launch-time",
        ),
        StartupSource(
            id = StartupSourceIds.ZYGOTE,
            title = "About the Zygote processes",
            url = "https://source.android.com/docs/core/runtime/zygote",
        ),
        StartupSource(
            id = StartupSourceIds.PROCESSES_AND_THREADS,
            title = "Processes and threads overview",
            url = "https://developer.android.com/guide/components/processes-and-threads",
        ),
        StartupSource(
            id = StartupSourceIds.APPLICATION_ON_CREATE,
            title = "Application.onCreate",
            url = "https://developer.android.com/reference/android/app/Application#onCreate()",
        ),
        StartupSource(
            id = StartupSourceIds.CONTENT_PROVIDER,
            title = "ContentProvider",
            url = "https://developer.android.com/reference/android/content/ContentProvider",
        ),
        StartupSource(
            id = StartupSourceIds.ACTIVITY_LIFECYCLE,
            title = "The activity lifecycle",
            url = "https://developer.android.com/guide/components/activities/activity-lifecycle",
        ),
        StartupSource(
            id = StartupSourceIds.COMPOSE_PHASES,
            title = "Jetpack Compose phases",
            url = "https://developer.android.com/develop/ui/compose/phases",
        ),
        StartupSource(
            id = StartupSourceIds.APP_STARTUP_LIBRARY,
            title = "App Startup",
            url = "https://developer.android.com/topic/libraries/app-startup",
        ),
        StartupSource(
            id = StartupSourceIds.SYSTEM_CLOCK,
            title = "SystemClock",
            url = "https://developer.android.com/reference/android/os/SystemClock",
        ),
        StartupSource(
            id = StartupSourceIds.VIEW_TREE_OBSERVER,
            title = "ViewTreeObserver.OnDrawListener",
            url = "https://developer.android.com/reference/android/view/ViewTreeObserver.OnDrawListener",
        ),
    ).associateBy(StartupSource::id)

    val coldStartStages: List<StartupStage> = listOf(
        StartupStage(
            id = "launch-request",
            order = 1,
            lane = StartupLane.USER_LAUNCHER,
            title = "Launch request",
            summary = "Một entry point yêu cầu Android mở component đích.",
            whatHappens = "Launcher icon, deep link, notification hoặc component khác gửi một launch request. Android bắt đầu resolve intent, task và component cần chạy.",
            whereItRuns = "Bắt đầu ở entry-point process rồi đi qua Binder vào system_server.",
            whyItMatters = "Cold start không đồng nghĩa mọi lần đều đi qua launcher activity; entry point quyết định phần flow kế tiếp.",
            staffNote = "Khi điều tra startup, hãy ghi rõ entry point. Một trace từ launcher icon không chứng minh flow của deep link, service, receiver hoặc provider.",
            sourceIds = listOf(StartupSourceIds.APP_STARTUP_TIME),
        ),
        StartupStage(
            id = "resolve-and-schedule",
            order = 2,
            lane = StartupLane.SYSTEM_SERVER,
            title = "Resolve & schedule",
            summary = "System chọn task/component và kiểm tra app process.",
            whatHappens = "system_server resolve request, chuẩn bị task/activity launch và kiểm tra process đích đã tồn tại hay chưa. Trong cold start, system có thể hiện starting window trong khi app chưa vẽ frame đầu.",
            whereItRuns = "system_server; starting window do system quản lý.",
            whyItMatters = "Phần thời gian trước khi app code chạy vẫn thuộc trải nghiệm launch mà người dùng cảm nhận.",
            staffNote = "Đừng gán toàn bộ startup latency cho Application hoặc Activity. Trace phải giữ cả system-side slices và app-side slices.",
            sourceIds = listOf(StartupSourceIds.APP_STARTUP_TIME),
        ),
        StartupStage(
            id = "fork-or-specialize",
            order = 3,
            lane = StartupLane.ZYGOTE_USAP,
            title = "Fork or specialize",
            summary = "Nếu chưa có process, system yêu cầu tạo app process.",
            whatHappens = "system_server gửi yêu cầu qua Unix domain socket. Zygote fork process theo nhu cầu, hoặc một USAP có sẵn được specialize cho application.",
            whereItRuns = "Boundary giữa system_server và Zygote; USAP chỉ tham gia khi pool được bật.",
            whyItMatters = "App process creation là công việc riêng của cold start và không xuất hiện giống nhau ở warm/hot start.",
            staffNote = "USAP là platform implementation detail tùy cấu hình, không phải bước bắt buộc trên mọi device. UI và tài liệu nên diễn đạt theo nhánh tùy chọn.",
            sourceIds = listOf(StartupSourceIds.ZYGOTE),
        ),
        StartupStage(
            id = "prepare-app-process",
            order = 4,
            lane = StartupLane.APP_MAIN_THREAD,
            title = "Prepare app process",
            summary = "Runtime được bind và main thread bắt đầu xử lý app work.",
            whatHappens = "Process mới thiết lập Android runtime và main thread để framework có thể tạo các application component.",
            whereItRuns = "App Linux process; component callbacks mặc định được dispatch trên main thread của process đó.",
            whyItMatters = "Main thread là một serialized critical path: blocking I/O hoặc heavy computation sẽ trì hoãn callback và drawing phía sau.",
            staffNote = "Application tồn tại theo process. App dùng nhiều process có thể tạo nhiều Application instance; đừng dùng mental model “một lần cho cả đời bản cài”.",
            sourceIds = listOf(StartupSourceIds.PROCESSES_AND_THREADS),
        ),
        StartupStage(
            id = "create-application-and-providers",
            order = 5,
            lane = StartupLane.APP_MAIN_THREAD,
            title = "Create Application & providers",
            summary = "Framework tạo Application và manifest ContentProvider.",
            whatHappens = "Framework tạo Application object, sau đó khởi tạo các manifest ContentProvider trước khi gọi Application.onCreate(). ContentProvider.onCreate() chạy trên application main thread.",
            whereItRuns = "App process · main thread, trước callback Application.onCreate().",
            whyItMatters = "SDK auto-init qua provider có thể thêm công việc ẩn vào critical path trước khi app sở hữu callback Application.onCreate().",
            staffNote = "Không giả định thứ tự giữa nhiều provider. Jetpack App Startup gom initializer vào một provider và cho khai báo dependency order; phần không cần ngay nên được lazy-init.",
            sourceIds = listOf(
                StartupSourceIds.APPLICATION_ON_CREATE,
                StartupSourceIds.CONTENT_PROVIDER,
                StartupSourceIds.APP_STARTUP_LIBRARY,
            ),
        ),
        StartupStage(
            id = "application-on-create",
            order = 6,
            lane = StartupLane.APP_MAIN_THREAD,
            title = "Application.onCreate()",
            summary = "Global app initialization chạy trước Activity đầu tiên.",
            whatHappens = "Framework gọi Application.onCreate() trước khi tạo Activity, Service hoặc Receiver đầu tiên trong process; ContentProvider là ngoại lệ đã được tạo trước đó.",
            whereItRuns = "App process · main thread.",
            whyItMatters = "Thời gian trong callback này trực tiếp trì hoãn component đầu tiên và first frame.",
            staffNote = "Chỉ giữ initialization bắt buộc cho first useful frame. Ưu tiên lazy initialization; không network, disk read hay heavy graph construction trên main thread.",
            sourceIds = listOf(
                StartupSourceIds.APPLICATION_ON_CREATE,
                StartupSourceIds.APP_STARTUP_TIME,
            ),
        ),
        StartupStage(
            id = "activity-lifecycle",
            order = 7,
            lane = StartupLane.APP_MAIN_THREAD,
            title = "Launch Activity",
            summary = "Activity đi qua onCreate → onStart → onResume.",
            whatHappens = "Với launcher-activity flow, system tạo Activity và gọi onCreate(), onStart(), rồi onResume() theo thứ tự trước khi Activity ở foreground tương tác.",
            whereItRuns = "App process · main thread.",
            whyItMatters = "Activity.onCreate() thường chứa setContent và initialization UI nên nằm trên đường tới frame đầu.",
            staffNote = "Lifecycle order là contract, nhưng launcher Activity không phải universal entry point. Tối ưu theo component thực tế được launch và state process/activity hiện có.",
            sourceIds = listOf(
                StartupSourceIds.ACTIVITY_LIFECYCLE,
                StartupSourceIds.APP_STARTUP_TIME,
            ),
        ),
        StartupStage(
            id = "compose-phases",
            order = 8,
            lane = StartupLane.APP_MAIN_THREAD,
            title = "Compose initial UI",
            summary = "setContent dẫn tới composition → layout → draw.",
            whatHappens = "Initial composition xác định UI, layout measure/place các node, rồi drawing tạo lệnh vẽ cho frame.",
            whereItRuns = "Initial Compose work nằm trong app UI pipeline; composition và phần UI work quan trọng xuất hiện trên main thread trace.",
            whyItMatters = "Hierarchy sâu, heavy calculation trong composable và resource decoding có thể làm frame đầu đến muộn.",
            staffNote = "Đọc phase-aware trace thay vì chỉ đếm recomposition. Defer UI không critical và tránh tính toán nặng trong composition.",
            sourceIds = listOf(
                StartupSourceIds.COMPOSE_PHASES,
                StartupSourceIds.APP_STARTUP_TIME,
            ),
        ),
        StartupStage(
            id = "first-frame-and-fully-drawn",
            order = 9,
            lane = StartupLane.RENDER_PIPELINE,
            title = "First frame → usable",
            summary = "First frame đánh dấu TTID; usable state cần TTFD.",
            whatHappens = "Khi app hoàn tất first draw, system thay starting window bằng Activity window. TTID dừng tại first frame; TTFD gồm cả content async cần thiết cho trạng thái usable.",
            whereItRuns = "App/render pipeline hoàn tất frame; system quản lý window transition và metric.",
            whyItMatters = "UI xuất hiện nhanh chưa chắc đã tương tác được hoặc có primary content. Staff-level optimization theo dõi cả TTID và TTFD.",
            staffNote = "System tự xác định TTID nhưng app phải signal fully drawn cho TTFD đúng thời điểm. Đừng reportFullyDrawn trước khi primary content thực sự usable.",
            sourceIds = listOf(StartupSourceIds.APP_STARTUP_TIME),
        ),
    )

    val staffNotes: List<StaffNote> = listOf(
        StaffNote(
            id = "entry-point-changes-flow",
            title = "Entry point changes the trace",
            body = "Launcher Activity chỉ là một startup path. Deep link, notification, service, receiver hoặc provider có thể tạo component path khác, vì vậy hãy ghi rõ trigger khi so sánh trace.",
            sourceIds = listOf(StartupSourceIds.APP_STARTUP_TIME),
        ),
        StaffNote(
            id = "usap-is-conditional",
            title = "USAP is conditional",
            body = "USAP pool không phải bước bắt buộc trên mọi device. Zygote có thể specialize một pooled USAP khi pool được bật, hoặc fork process theo nhu cầu.",
            sourceIds = listOf(StartupSourceIds.ZYGOTE),
        ),
        StaffNote(
            id = "provider-order-is-partial",
            title = "Provider order is only partial",
            body = "Manifest provider được tạo trước Application.onCreate(), nhưng không có thứ tự tổng quát giữa các provider độc lập. App Startup chỉ tạo dependency order cho các initializer đã khai báo quan hệ.",
            sourceIds = listOf(
                StartupSourceIds.APPLICATION_ON_CREATE,
                StartupSourceIds.CONTENT_PROVIDER,
                StartupSourceIds.APP_STARTUP_LIBRARY,
            ),
        ),
        StaffNote(
            id = "application-is-per-process",
            title = "Application is per process",
            body = "Components mặc định cùng chạy trong một process, nhưng manifest có thể tách process. Mỗi app process có lifecycle và Application instance riêng.",
            sourceIds = listOf(
                StartupSourceIds.PROCESSES_AND_THREADS,
                StartupSourceIds.APPLICATION_ON_CREATE,
            ),
        ),
        StaffNote(
            id = "ttid-is-not-usable",
            title = "TTID is not TTFD",
            body = "First frame đánh dấu TTID nhưng không đồng nghĩa primary content đã usable. TTFD cần app signal fully drawn sau khi UI và dữ liệu cần thiết thực sự sẵn sàng.",
            sourceIds = listOf(StartupSourceIds.APP_STARTUP_TIME),
        ),
        StaffNote(
            id = "observed-frame-is-not-ttid",
            title = "Observed frame is not a benchmark",
            body = "Lab dùng elapsedRealtimeNanos monotonic và callback khi view tree sắp draw. Event này là observation có observer cost, không phải TTID do Android Framework report và không thay thế Macrobenchmark/Perfetto.",
            sourceIds = listOf(
                StartupSourceIds.SYSTEM_CLOCK,
                StartupSourceIds.VIEW_TREE_OBSERVER,
                StartupSourceIds.APP_STARTUP_TIME,
            ),
        ),
    )

    init {
        require(coldStartStages.map(StartupStage::order) == (1..coldStartStages.size).toList()) {
            "Cold-start stages must stay in contiguous learning order."
        }
        require(coldStartStages.map(StartupStage::id).distinct().size == coldStartStages.size) {
            "Cold-start stage IDs must be unique."
        }
        require(coldStartStages.flatMap(StartupStage::sourceIds).all(sources::containsKey)) {
            "Every startup claim must resolve to a stable official source ID."
        }
        require(staffNotes.map(StaffNote::id).distinct().size == staffNotes.size) {
            "Staff-note IDs must be unique."
        }
        require(staffNotes.all { note -> note.sourceIds.isNotEmpty() && note.sourceIds.all(sources::containsKey) }) {
            "Every staff note must resolve to at least one official source ID."
        }
    }
}
