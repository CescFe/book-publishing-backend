package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class EmailTest {
    @Test
    fun `should create email with valid format`() {
        // When
        val email = Email("tolkien@example.com")

        // Then
        assertEquals("tolkien@example.com", email.value)
    }

    @Test
    fun `should throw AuthorValidationException when email is blank`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Email("   ")
        }
    }

    @Test
    fun `should throw AuthorValidationException when email does not contain @`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Email("invalid-email")
        }
    }

    @Test
    fun `should throw AuthorValidationException when email format is invalid`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Email("@example.com")
        }
    }
}
