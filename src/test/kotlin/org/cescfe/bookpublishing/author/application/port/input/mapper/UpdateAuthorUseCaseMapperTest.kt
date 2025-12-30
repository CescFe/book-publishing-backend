package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.UpdateAuthorCommandObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UpdateAuthorUseCaseMapperTest {
    private val mapper = UpdateAuthorUseCaseMapper()

    @Test
    fun `should map input values to domain author`() {
        // Given
        val existingAuthor = AuthorObjectMother.createWithAllFields()
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Updated Author Name",
            pseudonym = "Updated Pseudonym",
            biography = "Updated biography text",
            email = "updated@example.com",
            website = "https://www.updated-website.com",
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(existingAuthor.id, result.id)
        assertEquals(input.fullName, result.fullName.value)
        assertEquals(input.pseudonym, result.pseudonym!!.value)
        assertEquals(input.biography, result.biography!!.value)
        assertEquals(input.email, result.email!!.value)
        assertEquals(input.website, result.website!!.value)
    }

    @Test
    fun `should map input values with minimal fields`() {
        // Given
        val existingAuthor = AuthorObjectMother.createMinimal()
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Updated Minimal Author",
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(existingAuthor.id, result.id)
        assertEquals(input.fullName, result.fullName.value)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }

    @Test
    fun `should preserve existing author id`() {
        // Given
        val existingAuthorId = java.util.UUID.randomUUID()
        val existingAuthor = AuthorObjectMother.create(
            id = existingAuthorId,
            fullName = "Original Author",
        )
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Updated Author",
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(existingAuthorId, result.id.value)
    }

    @Test
    fun `should map all optional fields when provided`() {
        // Given
        val existingAuthor = AuthorObjectMother.createMinimal()
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Complete Author",
            pseudonym = "Pseudonym",
            biography = "A detailed biography",
            email = "author@example.com",
            website = "https://www.author-website.com",
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(input.fullName, result.fullName.value)
        assertEquals(input.pseudonym, result.pseudonym!!.value)
        assertEquals(input.biography, result.biography!!.value)
        assertEquals(input.email, result.email!!.value)
        assertEquals(input.website, result.website!!.value)
    }

    @Test
    fun `should handle null optional fields correctly`() {
        // Given
        val existingAuthor = AuthorObjectMother.createWithAllFields()
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Author Without Optional Fields",
            pseudonym = null,
            biography = null,
            email = null,
            website = null,
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(input.fullName, result.fullName.value)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }

    @Test
    fun `should update only specific fields while preserving id`() {
        // Given
        val existingAuthor = AuthorObjectMother.createWithAllFields()
        val input = UpdateAuthorCommandObjectMother.create(
            authorId = existingAuthor.id.value.toString(),
            fullName = "Partially Updated Author",
            pseudonym = "New Pseudonym",
        )

        // When
        val result = mapper.toDomain(input, existingAuthor)

        // Then
        assertEquals(existingAuthor.id, result.id)
        assertEquals(input.fullName, result.fullName.value)
        assertEquals(input.pseudonym, result.pseudonym!!.value)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }
}
