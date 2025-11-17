package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class PageCountTest {
    @Test
    fun `should create page count with valid data`() {
        // When
        val pageCount = PageCount(448)

        // Then
        assertEquals(448, pageCount.value)
    }

    @Test
    fun `should create page count with minimum value`() {
        // When
        val pageCount = PageCount(1)

        // Then
        assertEquals(1, pageCount.value)
    }

    @Test
    fun `should throw BookDomainException when page count is zero`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                PageCount(0)
            }
        assertEquals("Page count must be at least 1", exception.message)
    }

    @Test
    fun `should throw BookDomainException when page count is negative`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                PageCount(-1)
            }
        assertEquals("Page count must be at least 1", exception.message)
    }
}
