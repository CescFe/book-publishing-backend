package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthorInteractorTest {
    private val authorRepository: AuthorRepository = mock()
    private val getAuthorUseCase = GetAuthorInteractor(authorRepository)

    @Test
    fun `should return author when found`() {
        // Given
        val query = GetAuthorUseCase.Query(authorId = UUID.randomUUID().toString())
        val authorId = AuthorId.fromString(query.authorId)
        val expectedAuthor = AuthorObjectMother.createTolkien()

        whenever(authorRepository.findById(authorId)).thenReturn(expectedAuthor)

        // When
        val result = getAuthorUseCase.execute(query)

        // Then
        assertEquals(expectedAuthor, result)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should return AuthorDomainException when author not found`() {
        // Given
        val query = GetAuthorUseCase.Query(authorId = UUID.randomUUID().toString())
        val authorId = AuthorId.fromString(query.authorId)

        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                getAuthorUseCase.execute(query)
            }
        assertEquals("Author with id ${query.authorId} not found", exception.message)
        assertEquals("AUTHOR_NOT_FOUND", exception.subType)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should throw exception when authorId is invalid`() {
        // Given
        val query = GetAuthorUseCase.Query(authorId = "invalid-uuid")

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                getAuthorUseCase.execute(query)
            }
        assertEquals("Author id 'invalid-uuid' has invalid format. Expected a valid UUID", exception.message)
        assertEquals("AUTHOR_ID_INVALID_FORMAT", exception.subType)
    }
}
