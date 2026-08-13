package com.krahs.androidstafflab.feature.library

import androidx.compose.runtime.Immutable

@Immutable
data class Topic(
    val id: String,
    val sequence: String,
    val title: String,
    val question: String,
    val summary: String,
)

data class TopicCategory(
    val id: String,
    val sequence: String,
    val symbol: String,
    val title: String,
    val summary: String,
    val topics: List<Topic>,
)

object Topics {
    val applicationStartup = Topic(
        id = "application-startup",
        sequence = "01",
        title = "Application startup",
        question = "What happens when an Android application starts?",
        summary = "Trace the launch request through Android system services, Zygote, the app process, and the first frame.",
    )
}

object TopicCategories {
    val androidPlatform = TopicCategory(
        id = "android-platform",
        sequence = "01",
        symbol = "AP",
        title = "Android Platform",
        summary = "Understand how Android coordinates processes, components, runtime, and system services.",
        topics = listOf(Topics.applicationStartup),
    )
}
