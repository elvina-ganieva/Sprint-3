package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.time.*

internal class HrDepartmentTest {
    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun setUp() {
        certificateRequest = mockk()
    }

    @Test
    fun `receiveRequest() on LABOUR_BOOK_THURSDAY should push certificateRequest`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-02T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        //when
        assertDoesNotThrow("Should not throw an exception") {
            HrDepartment.receiveRequest(certificateRequest)
        }

        //then
        verify(exactly = 1) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals(listOf(certificateRequest), HrDepartment.incomeBox, "Should be equal")
    }

    @Test
    fun `receiveRequest() on LABOUR_BOOK_FRIDAY should throw exception`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-03T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        //when
        val exception = assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        //then
        verify(exactly = 1) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    fun `receiveRequest() on LABOUR_BOOK_WEEKEND should throw exception`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-04T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        //when
        val exception = assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        //then
        verify(exactly = 0) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun `receiveRequest() on NDFL_THURSDAY should throw exception`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-02T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        //when
        val exception = assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        //then
        verify(exactly = 1) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals("Не разрешено принять запрос на справку", exception.message)
    }

    @Test
    fun `receiveRequest() on NDFL_FRIDAY should push certificateRequest`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-03T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        //when
        assertDoesNotThrow("Should not throw an exception") {
            HrDepartment.receiveRequest(certificateRequest)
        }
        //then
        verify(exactly = 1) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals(listOf(certificateRequest), HrDepartment.incomeBox, "Should be equal")
    }

    @Test
    fun `receiveRequest() on NDFL_WEEKEND should throw exception`() {
        //given
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2021-09-04T10:00:00Z"),
            ZoneOffset.UTC)
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        //when
        val exception = assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }

        //then
        verify(exactly = 0) {
            certificateRequest.certificateType
        }
        confirmVerified(certificateRequest)
        assertEquals("Заказ справков в выходной день не работает", exception.message)
    }

    @Test
    fun `processNextRequestTest() should push certificate`() {
        //given
        val certificate = mockk<Certificate>()
        every { certificateRequest.process(1) } returns certificate
        HrDepartment.incomeBox.push(certificateRequest)

        //when
        HrDepartment.processNextRequest(1)

        //then
        verify (exactly = 1) {
            certificateRequest.process(1)
        }
        confirmVerified(certificateRequest)
        assertEquals(listOf(certificate), HrDepartment.outcomeOutcome, "Should be equal")
    }

    @AfterEach
    fun tearDown() {
        HrDepartment.incomeBox.clear()
        HrDepartment.outcomeOutcome.clear()
        unmockkAll()
    }
}
