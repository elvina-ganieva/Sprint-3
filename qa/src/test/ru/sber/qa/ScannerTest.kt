package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class ScannerTest {

    @BeforeEach
    fun setUp() {
        mockkObject(Random.Default)
    }

    @Test
    fun `getScanData() should return byteArray`() {
        //given
        every { Random.Default.nextLong(5000L, 15000L) } returns 7000L

        //when
        assertDoesNotThrow("Should not throw an exception") {
            Scanner.getScanData()
        }

        //then
        verify(exactly = 1) {
            Random.Default.nextLong(5000L, 15000L)
            Random.Default.nextBytes(100)
        }
        confirmVerified(Random.Default)
    }

    @Test
    fun `getScanData() should throw exception`() {
        //given
        every { Random.Default.nextLong(5000L, 15000L) } returns 11000L

        //when
        val exception = assertThrows<ScanTimeoutException> {
            Scanner.getScanData()
        }

        //then
        assertEquals("Таймаут сканирования документа", exception.message)
        verify(exactly = 1) {
            Random.Default.nextLong(5000L, 15000L)
        }
        verify(exactly = 0) {
            Random.Default.nextBytes(100)
        }
        confirmVerified(Random.Default)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}
