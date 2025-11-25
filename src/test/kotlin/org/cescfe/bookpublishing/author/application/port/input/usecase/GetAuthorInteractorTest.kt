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

    companion object {
        private const val NOT_FOUND_MESSAGE = "Author with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "AUTHOR_NOT_FOUND"
        private const val INVALID_UUID = "invalid-uuid"
        private const val INVALID_UUID_MESSAGE = "Author id '%s' has invalid format. Expected a valid UUID"
        private const val INVALID_UUID_SUBTYPE = "AUTHOR_ID_INVALID_FORMAT"
    }

    @Test
    fun `should return author when found`() {
        // Given
        val query = GetAuthorUseCase.Query(authorId = UUID.randomUUID().toString())
        val authorId = AuthorId.fromString(query.authorId)
        val expectedAuthor = AuthorObjectMother.createWithAllFields()

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
        assertEquals(String.format(NOT_FOUND_MESSAGE, query.authorId), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
        verify(authorRepository).findById(authorId)
    }

    @Test
    fun `should throw exception when authorId is invalid`() {
        // Given
        val query = GetAuthorUseCase.Query(authorId = INVALID_UUID)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                getAuthorUseCase.execute(query)
            }
        assertEquals(String.format(INVALID_UUID_MESSAGE, query.authorId), exception.message)
        assertEquals(INVALID_UUID_SUBTYPE, exception.subType)
    }
}
