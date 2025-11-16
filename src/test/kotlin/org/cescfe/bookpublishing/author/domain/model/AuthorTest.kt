package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AuthorTest {
    @Test
    fun `should create author with valid data`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val pseudonym = Pseudonym("J.R.R. Tolkien")
        val email = Email("tolkien@example.com")
        val website = Website("https://www.tolkiensociety.org")
        val biography = Biography("John Ronald Reuel Tolkien was an English writer...")

        // When
        val author =
            Author(
                id = id,
                fullName = fullName,
                pseudonym = pseudonym,
                email = email,
                website = website,
                biography = biography,
            )

        // Then
        assertEquals(id, author.id)
        assertEquals(fullName, author.fullName)
        assertEquals(pseudonym, author.pseudonym)
        assertEquals(email, author.email)
        assertEquals(website, author.website)
        assertEquals(biography, author.biography)
    }
}

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

class WebsiteTest {
    @Test
    fun `should create website with valid URL`() {
        // When
        val website = Website("https://www.tolkiensociety.org")

        // Then
        assertEquals("https://www.tolkiensociety.org", website.value)
    }

    @Test
    fun `should throw AuthorValidationException when website is blank`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Website("   ")
        }
    }

    @Test
    fun `should throw AuthorValidationException when website does not start with http`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Website("www.example.com")
        }
    }
}
