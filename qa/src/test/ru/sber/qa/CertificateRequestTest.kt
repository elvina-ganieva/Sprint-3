package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class CertificateRequestTest {

    private val certificateRequest = CertificateRequest(12345, CertificateType.NDFL)

    @Test
    fun `process() should return initialized certificate`() {
        //given
        mockkObject(Random.Default)
        every { Random.Default.nextLong(5000L, 15000L) } returns 7000L

        //when
        val certificate = certificateRequest.process(1)

        //then
        assertEquals(1, certificate.processedBy)
        assertEquals(certificateRequest, certificate.certificateRequest)
    }

}