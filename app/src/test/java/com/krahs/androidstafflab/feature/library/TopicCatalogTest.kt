package com.krahs.androidstafflab.feature.library

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class TopicCatalogTest {
    @Test
    fun androidPlatform_containsApplicationStartupEntry() {
        val category = TopicCategories.androidPlatform

        assertEquals("android-platform", category.id)
        assertEquals("01", category.sequence)
        assertEquals("AP", category.symbol)
        assertEquals("Android Platform", category.title)
        assertEquals(1, category.topics.size)
        assertSame(Topics.applicationStartup, category.topics.single())
        assertEquals(
            "What happens when an Android application starts?",
            category.topics.single().question,
        )
    }
}
