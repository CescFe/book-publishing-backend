package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AuthorTest {
    @Test
    fun `should create author with valid data`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val roles = setOf(AuthorRole.AUTHOR)
        val pseudonym = Pseudonym("J.R.R. Tolkien")
        val email = Email("tolkien@example.com")
        val website = Website("https://www.tolkiensociety.org")
        val biography = Biography("John Ronald Reuel Tolkien was an English writer...")

        // When
        val author =
            Author(
                id = id,
                fullName = fullName,
                roles = roles,
                pseudonym = pseudonym,
                email = email,
                website = website,
                biography = biography,
            )

        // Then
        assertEquals(id, author.id)
        assertEquals(fullName, author.fullName)
        assertEquals(roles, author.roles)
        assertEquals(pseudonym, author.pseudonym)
        assertEquals(email, author.email)
        assertEquals(website, author.website)
        assertEquals(biography, author.biography)
    }

    @Test
    fun `should throw InvalidAuthorRolesException when roles is empty`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val emptyRoles = emptySet<AuthorRole>()

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Author(
                    id = id,
                    fullName = fullName,
                    roles = emptyRoles,
                )
            }
        assertEquals("Author must have at least one role", exception.message)
    }

    @Test
    fun `should throw InvalidAuthorRolesException when roles does not contain AUTHOR`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val rolesWithoutAuthor = setOf(AuthorRole.ILLUSTRATOR)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                Author(
                    id = id,
                    fullName = fullName,
                    roles = rolesWithoutAuthor,
                )
            }
        assertEquals("Author must have AUTHOR role", exception.message)
    }

    @Test
    fun `should create author with multiple roles`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR)

        // When
        val author =
            Author(
                id = id,
                fullName = fullName,
                roles = roles,
            )

        // Then
        assertEquals(roles, author.roles)
        assertTrue(author.roles.contains(AuthorRole.AUTHOR))
        assertTrue(author.roles.contains(AuthorRole.ILLUSTRATOR))
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

class AuthorRoleTest {
    @Test
    fun `should create role from string`() {
        // When
        val role = AuthorRole.fromString("AUTHOR")

        // Then
        assertEquals(AuthorRole.AUTHOR, role)
    }

    @Test
    fun `should throw exception for unknown role`() {
        // When & Then
        assertThrows<IllegalArgumentException> {
            AuthorRole.fromString("UNKNOWN")
        }
    }
}
