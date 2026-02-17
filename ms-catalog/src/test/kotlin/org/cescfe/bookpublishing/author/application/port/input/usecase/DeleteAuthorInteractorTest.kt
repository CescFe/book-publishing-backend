package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteAuthorInteractorTest {
    private val authorRepository: AuthorRepository = mock()
    private val deleteAuthorUseCase = DeleteAuthorInteractor(authorRepository)

    @Test
    fun `should delete author successfully`() {
        // Given
        val command = DeleteAuthorUseCase.Command(UUID.randomUUID().toString())
        val authorId = AuthorId.fromString(command.authorId)
        val author = AuthorObjectMother.create()

        whenever(authorRepository.findById(authorId)).thenReturn(author)

        // When
        deleteAuthorUseCase.execute(command)

        // Then
        verify(authorRepository).findById(authorId)
        verify(authorRepository).deleteById(authorId)
    }

    @Test
    fun `should throw exception when author not found`() {
        // Given
        val command = DeleteAuthorUseCase.Command(UUID.randomUUID().toString())
        val authorId = AuthorId.fromString(command.authorId)

        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                deleteAuthorUseCase.execute(command)
            }
        assertEquals("Author with id ${command.authorId} not found", exception.message)
        assertEquals("AUTHOR_NOT_FOUND", exception.subType)
        verify(authorRepository).findById(authorId)
    }
}
