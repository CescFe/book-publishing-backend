package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
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
}
