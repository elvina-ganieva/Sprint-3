package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

internal class ScannerTest {

    @Test
    fun `getScanData() should throw exception`() {
        //given
        mockkObject(Random.Default)
        every { Random.Default.nextLong(5000L, 15000L) } returns 11000L

        //when
        val exception = assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }

        //then
        assertEquals("Таймаут сканирования документа", exception.message)
    }

    @Test
    fun `getScanData() should return byteArray`() {
        //given
        mockkObject(Random.Default)
        every { Random.Default.nextLong(5000L, 15000L) } returns 7000L

        //when
        assertDoesNotThrow("Should not throw an exception") {
            Scanner.getScanData()
        }

        //then
    }
}