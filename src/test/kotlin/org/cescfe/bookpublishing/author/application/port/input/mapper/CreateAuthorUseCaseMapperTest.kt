package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CreateAuthorUseCaseMapperTest {
    private val mapper = CreateAuthorUseCaseMapper()

    @Test
    fun `should map input values to domain author`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.createTolkien()

        // When
        val result = mapper.toDomain(input)

        // Then
        assertNotNull(result)
        assertEquals(input.fullName, result.fullName.value)
        assertEquals(input.roles, result.roles.map { it.name }.toSet())
        assertEquals(input.pseudonym, result.pseudonym!!.value)
        assertEquals(input.biography, result.biography!!.value)
        assertEquals(input.email, result.email!!.value)
        assertEquals(input.website, result.website!!.value)
    }

    @Test
    fun `should map input values with minimal fields`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.createMinimal()

        // When
        val result = mapper.toDomain(input)

        // Then
        assertNotNull(result)
        assertEquals(input.fullName, result.fullName.value)
        assertEquals(input.roles, result.roles.map { it.name }.toSet())
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }

    @Test
    fun `should map domain author to input values`() {
        // Given
        val author = AuthorObjectMother.createTolkien()

        // When
        val result = mapper.toInputValues(author)

        // Then
        assertEquals(author.fullName.value, result.fullName)
        assertEquals(author.roles.map { it.name }.toSet(), result.roles)
        assertEquals(author.pseudonym!!.value, result.pseudonym)
        assertEquals(author.biography!!.value, result.biography)
        assertEquals(author.email!!.value, result.email)
        assertEquals(author.website!!.value, result.website)
    }


    @Test
    fun `should throw AuthorDomainException when roles is empty`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = emptySet()
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Author must have at least one role", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when roles does not contain AUTHOR`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = setOf("ILLUSTRATOR")
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Author must have AUTHOR role", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when fullName is blank`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "",
            roles = setOf("AUTHOR")
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Full name cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when fullName is too long`() {
        // Given
        val longName = "A".repeat(256)
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = longName,
            roles = setOf("AUTHOR")
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Full name must be between 1 and 255 characters", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when pseudonym is blank`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = setOf("AUTHOR"),
            pseudonym = ""
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Pseudonym cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when biography is too long`() {
        // Given
        val longBiography = "A".repeat(2001)
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = setOf("AUTHOR"),
            biography = longBiography
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Biography cannot exceed 2000 characters", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when email format is invalid`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = setOf("AUTHOR"),
            email = "invalid-email"
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Email must contain @ symbol", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when website protocol is invalid`() {
        // Given
        val input = CreateAuthorInputValuesObjectMother.create(
            fullName = "Test Author",
            roles = setOf("AUTHOR"),
            website = "ftp://invalid.com"
        )

        // When & Then
        val exception = assertThrows<AuthorDomainException> {
            mapper.toDomain(input)
        }
        assertEquals("Website must start with http:// or https://", exception.message)
    }
}
