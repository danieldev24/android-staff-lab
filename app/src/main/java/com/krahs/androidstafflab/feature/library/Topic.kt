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

object Topics {
    val applicationStartup = Topic(
        id = "application-startup",
        sequence = "01",
        title = "Application startup",
        question = "What happens when an Android application starts?",
        summary = "Trace the launch request through Android system services, Zygote, the app process, and the first frame.",
    )
}
