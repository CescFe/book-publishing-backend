package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
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
        assertEquals(input.fullName, result.fullName.value)
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
        assertEquals(input.fullName, result.fullName.value)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }

    @Test
    fun `should throw AuthorDomainException when fullName is blank`() {
        // Given
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "",
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Full name cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when fullName is too long`() {
        // Given
        val longName = "A".repeat(256)
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = longName,
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Full name must be between 1 and 255 characters", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when pseudonym is blank`() {
        // Given
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "Test Author",
                pseudonym = "",
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Pseudonym cannot be blank", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when biography is too long`() {
        // Given
        val longBiography = "A".repeat(2001)
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "Test Author",
                biography = longBiography,
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Biography cannot exceed 2000 characters", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when email format is invalid`() {
        // Given
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "Test Author",
                email = "invalid-email",
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Email must contain @ symbol", exception.message)
    }

    @Test
    fun `should throw AuthorDomainException when website protocol is invalid`() {
        // Given
        val input =
            CreateAuthorInputValuesObjectMother.create(
                fullName = "Test Author",
                website = "ftp://invalid.com",
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Website must start with http:// or https://", exception.message)
    }
}
