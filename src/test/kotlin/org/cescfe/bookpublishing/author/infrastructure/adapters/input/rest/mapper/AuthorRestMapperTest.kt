package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthorRestMapperTest {
    private val mapper = AuthorRestMapper()

    @Test
    fun `should map author to DTO successfully`() {
        // Given
        val author = AuthorObjectMother.createWithAllFields()

        // When
        val result = mapper.toDto(author)

        // Then
        assertEquals(author.id.value, result.id)
        assertEquals(author.fullName.value, result.fullName)
        assertEquals(author.pseudonym!!.value, result.pseudonym)
        assertEquals(author.biography!!.value, result.biography)
        assertEquals(author.email!!.value, result.email)
        assertEquals(author.website!!.value, result.website.toString())
    }

    @Test
    fun `should map author with minimal fields to DTO`() {
        // Given
        val author = AuthorObjectMother.createMinimal()

        // When
        val result = mapper.toDto(author)

        // Then
        assertEquals(author.fullName.value, result.fullName)
        assertNull(result.pseudonym)
        assertNull(result.biography)
        assertNull(result.email)
        assertNull(result.website)
    }
}
