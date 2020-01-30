package com.matso.converter

import com.matso.converter.comman.deepEquals
import com.matso.converter.comman.toFlagEmoji
import junit.framework.TestCase.*
import org.junit.Test

class ExtensionsTest {
    companion object {
        const val us = "US"
        const val usFlagEmoji = "\uD83C\uDDFA\uD83C\uDDF8"
        const val usd = "USD"
    }


    @Test
    fun testListDeepEquals() {
        val listA = listOf("HI", "Hello")
        val listB = listOf("Hi", "HelLo")
        assertTrue(listA.deepEquals(listA))
        assertFalse(listA.deepEquals(listB))
    }

    @Test
    fun testRateToFlagEmoji() {
        assertEquals(us.toFlagEmoji(), usFlagEmoji)
        assertEquals(usd.toFlagEmoji(), usd)

    }

}
