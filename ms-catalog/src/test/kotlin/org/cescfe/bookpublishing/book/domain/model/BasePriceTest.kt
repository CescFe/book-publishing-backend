package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BasePriceTest {
    @Test
    fun `should create base price with valid data`() {
        // When
        val basePrice = BasePrice.fromDouble(19.95)

        // Then
        assertEquals(19.95, basePrice.value)
    }

    @Test
    fun `should create base price with zero`() {
        // When
        val basePrice = BasePrice.fromDouble(0.0)

        // Then
        assertEquals(0.0, basePrice.value)
    }

    @Test
    fun `should round base price to two decimal places`() {
        // When
        val basePrice = BasePrice.fromDouble(19.999)

        // Then
        assertEquals(20.0, basePrice.value)
    }

    @Test
    fun `should throw BookDomainException when base price is negative`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BasePrice(-10.0)
            }
        assertEquals("Base price cannot be negative", exception.message)
    }

    @Test
    fun `should throw BookDomainException when base price has more than two decimal places`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BasePrice(19.999)
            }
        assertEquals("Base price must have at most 2 decimal places", exception.message)
    }
}
