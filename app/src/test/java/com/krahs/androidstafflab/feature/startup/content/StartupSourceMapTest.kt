package com.krahs.androidstafflab.feature.startup.content

import java.net.URI
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupSourceMapTest {
    @Test
    fun everyStageAndStaffNote_resolvesToOfficialAndroidOrAospSources() {
        val allSourceIds = buildList {
            StartupContent.coldStartStages.forEach { stage ->
                assertFalse("Stage ${stage.id} needs a source", stage.sourceIds.isEmpty())
                addAll(stage.sourceIds)
            }
            StartupContent.staffNotes.forEach { note ->
                assertFalse("Staff note ${note.id} needs a source", note.sourceIds.isEmpty())
                addAll(note.sourceIds)
            }
        }

        allSourceIds.forEach { sourceId ->
            val source = requireNotNull(StartupContent.sources[sourceId])
            val uri = URI(source.url)
            assertEquals("https", uri.scheme)
            assertTrue(
                "${source.id} must be an official Android/AOSP URL",
                uri.host == "developer.android.com" || uri.host == "source.android.com",
            )
        }
    }

    @Test
    fun caveats_keepUsapProviderAndDisplayMetricsNonUniversal() {
        val notesById = StartupContent.staffNotes.associateBy(StaffNote::id)

        assertTrue(notesById.getValue("usap-is-conditional").body.contains("không phải"))
        assertTrue(notesById.getValue("provider-order-is-partial").body.contains("không có"))
        assertTrue(notesById.getValue("ttid-is-not-usable").body.contains("không đồng nghĩa"))
    }
}
