package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CoverImagePathTest {
    @Test
    fun `should create cover image path with valid data`() {
        // When
        val coverImagePath = CoverImagePath("covers/addie-larue.jpg")

        // Then
        assertEquals("covers/addie-larue.jpg", coverImagePath.value)
    }

    @Test
    fun `should create cover image path with exactly 255 characters`() {
        // Given
        val path255Chars = "a".repeat(255)

        // When
        val coverImagePath = CoverImagePath(path255Chars)

        // Then
        assertEquals(255, coverImagePath.value.length)
    }

    @Test
    fun `should throw BookDomainException when cover image path is too long`() {
        // Given
        val longPath = "a".repeat(256)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                CoverImagePath(longPath)
            }
        assertEquals("Cover image path cannot exceed 255 characters", exception.message)
    }
}
