package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class DescriptionTest {
    @Test
    fun `should create description with valid data`() {
        // When
        val description = Description("A Life No One Will Remember. A Story You Will Never Forget.")

        // Then
        assertEquals("A Life No One Will Remember. A Story You Will Never Forget.", description.value)
    }

    @Test
    fun `should create description with empty string`() {
        // When
        val description = Description("")

        // Then
        assertEquals("", description.value)
    }

    @Test
    fun `should create description with exactly 2000 characters`() {
        // Given
        val description2000Chars = "a".repeat(2000)

        // When
        val description = Description(description2000Chars)

        // Then
        assertEquals(2000, description.value.length)
    }

    @Test
    fun `should throw BookDomainException when description is too long`() {
        // Given
        val longDescription = "a".repeat(2001)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                Description(longDescription)
            }
        assertEquals("Description cannot exceed 2000 characters", exception.message)
    }
}
