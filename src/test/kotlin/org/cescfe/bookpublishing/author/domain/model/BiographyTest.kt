package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BiographyTest {
    @Test
    fun `should create biography with valid data`() {
        // When
        val biography = Biography("John Ronald Reuel Tolkien was an English writer and philologist...")

        // Then
        assertEquals("John Ronald Reuel Tolkien was an English writer and philologist...", biography.value)
    }

    @Test
    fun `should create biography with exactly 2000 characters`() {
        // Given
        val biography2000Chars = "a".repeat(2000)

        // When
        val biography = Biography(biography2000Chars)

        // Then
        assertEquals(2000, biography.value.length)
    }

    @Test
    fun `should create biography with empty string`() {
        // When
        val biography = Biography("")

        // Then
        assertEquals("", biography.value)
    }

    @Test
    fun `should create biography with whitespace only`() {
        // When
        val biography = Biography("   ")

        // Then
        assertEquals("   ", biography.value)
    }

    @Test
    fun `should throw AuthorDomainException when biography is too long`() {
        // Given
        val longBiography = "a".repeat(2001)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Biography(longBiography)
            }
        assertEquals("Biography cannot exceed 2000 characters", exception.message)
    }
}
