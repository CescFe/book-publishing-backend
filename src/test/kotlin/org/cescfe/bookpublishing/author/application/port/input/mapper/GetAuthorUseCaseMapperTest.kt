package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.objectMothers.GetAuthorInputValuesObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetAuthorUseCaseMapperTest {
    private val mapper = GetAuthorUseCaseMapper()

    companion object {
        private const val INVALID_UUID = "invalid-uuid"
    }

    @Test
    fun `should convert string to AuthorId`() {
        // Given
        val input = GetAuthorInputValuesObjectMother.createWithTolkienId()

        // When
        val result = mapper.toDomain(input.authorId)

        // Then
        assertEquals(AuthorId.fromString(input.authorId), result)
    }

    @Test
    fun `should throw exception when string is invalid UUID`() {
        // Given
        val input =
            GetAuthorInputValuesObjectMother.create(
                authorId = INVALID_UUID,
            )

        // When & Then
        assertThrows<IllegalArgumentException> {
            mapper.toDomain(input.authorId)
        }
    }

    @Test
    fun `should convert AuthorId to InputValues`() {
        // Given
        val authorId = AuthorId.generate()

        // When
        val result = mapper.toInputValues(authorId)

        // Then
        assertEquals(GetAuthorUseCase.InputValues(authorId.value.toString()), result)
    }
}
