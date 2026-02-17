package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class VatRateTest {
    @Test
    fun `should create VAT rate with valid data`() {
        // When
        val vatRate = VatRate.fromDouble(0.04)

        // Then
        assertEquals(0.04, vatRate.value)
    }

    @Test
    fun `should create default VAT rate`() {
        // When
        val vatRate = VatRate.default()

        // Then
        assertEquals(0.04, vatRate.value)
    }

    @Test
    fun `should create VAT rate with zero`() {
        // When
        val vatRate = VatRate.fromDouble(0.0)

        // Then
        assertEquals(0.0, vatRate.value)
    }

    @Test
    fun `should create VAT rate with one`() {
        // When
        val vatRate = VatRate.fromDouble(1.0)

        // Then
        assertEquals(1.0, vatRate.value)
    }

    @Test
    fun `should round VAT rate to two decimal places`() {
        // When
        val vatRate = VatRate.fromDouble(0.045)

        // Then
        assertEquals(0.05, vatRate.value)
    }

    @Test
    fun `should throw BookDomainException when VAT rate is negative`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                VatRate(-0.1)
            }
        assertEquals("VAT rate must be between 0 and 1", exception.message)
    }

    @Test
    fun `should throw BookDomainException when VAT rate is greater than one`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                VatRate(1.1)
            }
        assertEquals("VAT rate must be between 0 and 1", exception.message)
    }

    @Test
    fun `should throw BookDomainException when VAT rate has more than two decimal places`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                VatRate(0.045)
            }
        assertEquals("VAT rate must have at most 2 decimal places", exception.message)
    }
}
