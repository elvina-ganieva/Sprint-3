package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.*

internal class HrDepartmentTest {

    @Test
    fun receiveRequestTest_LABOUR_BOOK_THURSDAY() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-02T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertDoesNotThrow("Should not throw an exception") {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestTest_LABOUR_BOOK_FRIDAY() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-03T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        val exception = assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    fun receiveRequestTest_LABOUR_BOOK_WEEKEND() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-04T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        val exception = assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun receiveRequestTest_NDFL_THURSDAY() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-02T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.NDFL

        val exception = assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    fun receiveRequestTest_NDFL_FRIDAY() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-03T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertDoesNotThrow("Should not throw an exception") {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestTest_NDFL_WEEKEND() {

        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-04T10:00:00Z"),
            ZoneOffset.UTC)

        val certificateRequest = mockk<CertificateRequest>()

        every { certificateRequest.certificateType } returns CertificateType.NDFL

        val exception = assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun processNextRequestTest() {

    }
}