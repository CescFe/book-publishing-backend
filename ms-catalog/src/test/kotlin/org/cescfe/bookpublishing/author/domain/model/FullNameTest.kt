package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class FullNameTest {
    @Test
    fun `should create full name with valid data`() {
        // When
        val fullName = FullName("John Ronald Reuel Tolkien")

        // Then
        assertEquals("John Ronald Reuel Tolkien", fullName.value)
    }

    @Test
    fun `should throw AuthorValidationException when full name is blank`() {
        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                FullName("   ")
            }
        assertEquals("Full name cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorValidationException when full name is too long`() {
        // Given
        val longName = "a".repeat(256)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                FullName(longName)
            }
        assertEquals("Full name must be between 1 and 255 characters", exception.message)
    }
}
