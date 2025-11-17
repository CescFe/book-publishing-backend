package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BookTitleTest {
    @Test
    fun `should create book title with valid data`() {
        // When
        val title = BookTitle("The Invisible Life of Addie LaRue")

        // Then
        assertEquals("The Invisible Life of Addie LaRue", title.value)
    }

    @Test
    fun `should create book title with exactly 200 characters`() {
        // Given
        val title200Chars = "a".repeat(200)

        // When
        val title = BookTitle(title200Chars)

        // Then
        assertEquals(200, title.value.length)
    }

    @Test
    fun `should throw BookDomainException when title is blank`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BookTitle("   ")
            }
        assertEquals("Book title cannot be blank", exception.message)
    }

    @Test
    fun `should throw BookDomainException when title is empty string`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BookTitle("")
            }
        assertEquals("Book title cannot be blank", exception.message)
    }

    @Test
    fun `should throw BookDomainException when title is too long`() {
        // Given
        val longTitle = "a".repeat(201)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BookTitle(longTitle)
            }
        assertEquals("Book title must be between 1 and 200 characters", exception.message)
    }
}
