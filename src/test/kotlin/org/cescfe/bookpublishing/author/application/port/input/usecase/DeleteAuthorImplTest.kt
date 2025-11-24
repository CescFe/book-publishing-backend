package org.cescfe.bookpublishing.author.application.port.input.usecase

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.DeleteAuthorInputValuesObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteAuthorImplTest {
    private val authorRepository = mock<AuthorRepositoryView>()
    private val deleteAuthorUseCase = DeleteAuthorInteractor(authorRepository)

    @Test
    fun `should perform hard delete when author has only AUTHOR role`() {
        // Given
        val input = DeleteAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)
        val authorWithSingleRole =
            AuthorObjectMother.create(
                fullName = "J.R.R. Tolkien",
            )

        whenever(authorRepository.findById(authorId)).thenReturn(authorWithSingleRole)

        // When
        deleteAuthorUseCase.execute(input)

        // Then
        verify(authorRepository).findById(authorId)
        verify(authorRepository).deleteById(authorId)
    }

    @Test
    fun `should throw exception when author not found`() {
        // Given
        val input = DeleteAuthorInputValuesObjectMother.createWithTolkienId()
        val authorId = AuthorId.fromString(input.authorId)

        whenever(authorRepository.findById(authorId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                deleteAuthorUseCase.execute(input)
            }
        assertEquals("Author with id ${input.authorId} not found", exception.message)
        verify(authorRepository).findById(authorId)
    }
}
