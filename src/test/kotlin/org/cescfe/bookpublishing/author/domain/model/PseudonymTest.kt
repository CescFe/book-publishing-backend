package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class PseudonymTest {
    @Test
    fun `should create pseudonym with valid data`() {
        // When
        val pseudonym = Pseudonym("J.R.R. Tolkien")

        // Then
        assertEquals("J.R.R. Tolkien", pseudonym.value)
    }

    @Test
    fun `should create pseudonym with exactly 255 characters`() {
        // Given
        val pseudonym255Chars = "a".repeat(255)

        // When
        val pseudonym = Pseudonym(pseudonym255Chars)

        // Then
        assertEquals(255, pseudonym.value.length)
    }

    @Test
    fun `should create pseudonym with exactly 1 character`() {
        // When
        val pseudonym = Pseudonym("A")

        // Then
        assertEquals("A", pseudonym.value)
    }

    @Test
    fun `should throw AuthorDomainException when pseudonym is blank`() {
        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Pseudonym("   ")
            }
        assertEquals("Pseudonym cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when pseudonym is empty string`() {
        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Pseudonym("")
            }
        assertEquals("Pseudonym cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when pseudonym is too long`() {
        // Given
        val longPseudonym = "a".repeat(256)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Pseudonym(longPseudonym)
            }
        assertEquals("Pseudonym must be between 1 and 255 characters", exception.message)
    }
}
