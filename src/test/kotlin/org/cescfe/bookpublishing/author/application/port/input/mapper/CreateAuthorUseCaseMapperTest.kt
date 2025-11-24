package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.objectMothers.CreateAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
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
}
