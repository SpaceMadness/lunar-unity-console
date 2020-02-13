package spacemadness.com.lunarconsole.utils

import org.junit.Test

import org.junit.Assert.*

class RangeTest {
    @Test
    fun testRange() {
        Range(0, 0)
        Range(0, 10)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidRange() {
        Range(10, 0)
    }
}