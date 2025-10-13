package org.cescfe.bookpublishing.author.application.port.input.interactor

import org.cescfe.bookpublishing.author.application.port.input.mapper.GetAuthorUseCaseMapper
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.GetAuthorInputValuesObjectMother
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthorImplTest {
    private val authorRepository = mock<AuthorRepositoryView>()
    private val mapper = mock<GetAuthorUseCaseMapper>()
    private val getAuthorUseCase = GetAuthorImpl(authorRepository, mapper)

    @Test
    fun `should return author when found`() {
        // Given
        val input = GetAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)
        val expectedAuthor = AuthorObjectMother.createTolkien()

        whenever(mapper.toDomain(input.authorId)).thenReturn(authorId)
        whenever(authorRepository.findById(authorId)).thenReturn(expectedAuthor)

        // When
        val result = getAuthorUseCase.execute(input)

        // Then
        assertEquals(expectedAuthor, result)
        verify(mapper).toDomain(input.authorId)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should return null when author not found`() {
        // Given
        val input = GetAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)

        whenever(mapper.toDomain(input.authorId)).thenReturn(authorId)
        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When
        val result = getAuthorUseCase.execute(input)

        // Then
        assertNull(result)
        verify(mapper).toDomain(input.authorId)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should throw exception when authorId is invalid`() {
        // Given
        val input =
            GetAuthorInputValuesObjectMother.create(
                authorId = "invalid-uuid",
            )

        whenever(mapper.toDomain(input.authorId))
            .thenThrow(IllegalArgumentException("Invalid UUID string: ${input.authorId}"))

        // When & Then
        assertThrows<IllegalArgumentException> {
            getAuthorUseCase.execute(input)
        }
        verify(mapper).toDomain(input.authorId)
    }
}
