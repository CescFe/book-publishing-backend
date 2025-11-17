package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ISBNTest {
    @Test
    fun `should create ISBN with valid format starting with 978`() {
        // When
        val isbn = ISBN("9780547928227")

        // Then
        assertEquals("9780547928227", isbn.value)
    }

    @Test
    fun `should create ISBN with valid format starting with 979`() {
        // When
        val isbn = ISBN("9791234567890")

        // Then
        assertEquals("9791234567890", isbn.value)
    }

    @Test
    fun `should throw BookDomainException when ISBN does not start with 978 or 979`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                ISBN("9771234567890")
            }
        assertEquals("ISBN must be a valid ISBN-13 format (starting with 978 or 979 followed by 10 digits)", exception.message)
    }

    @Test
    fun `should throw BookDomainException when ISBN has wrong length`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                ISBN("978054792822")
            }
        assertEquals("ISBN must be a valid ISBN-13 format (starting with 978 or 979 followed by 10 digits)", exception.message)
    }

    @Test
    fun `should throw BookDomainException when ISBN contains non-numeric characters`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                ISBN("978054792822a")
            }
        assertEquals("ISBN must be a valid ISBN-13 format (starting with 978 or 979 followed by 10 digits)", exception.message)
    }
}
