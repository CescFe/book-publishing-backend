package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.GetAuthorInputValuesObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthorImplTest {
    private val authorRepository = mock<AuthorRepository>()
    private val getAuthorUseCase = GetAuthorInteractor(authorRepository)

    @Test
    fun `should return author when found`() {
        // Given
        val input = GetAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)
        val expectedAuthor = AuthorObjectMother.createTolkien()

        whenever(authorRepository.findById(authorId)).thenReturn(expectedAuthor)

        // When
        val result = getAuthorUseCase.execute(input)

        // Then
        assertEquals(expectedAuthor, result)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should return AuthorDomainException when author not found`() {
        // Given
        val input = GetAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)

        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                getAuthorUseCase.execute(input)
            }
        assertEquals("Author with id ${input.authorId} not found", exception.message)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should throw exception when authorId is invalid`() {
        // Given
        val input =
            GetAuthorInputValuesObjectMother.create(
                authorId = "invalid-uuid",
            )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                getAuthorUseCase.execute(input)
            }
        assertEquals("Author id 'invalid-uuid' has invalid format. Expected a valid UUID", exception.message)
        assertEquals("AUTHOR_ID_INVALID_FORMAT", exception.subType)
    }
}
