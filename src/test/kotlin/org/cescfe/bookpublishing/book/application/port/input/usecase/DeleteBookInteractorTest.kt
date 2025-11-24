package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.DeleteBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

class DeleteBookInteractorTest {
    private val bookRepository: BookRepository = mock()
    private val deleteBookUseCase = DeleteBookInteractor(bookRepository)

    @Test
    fun `should delete book successfully`() {
        // Given
        val command = DeleteBookUseCase.Command(UUID.randomUUID().toString())
        val bookId = BookId.fromString(command.bookId)
        val book = BookObjectMother.create()

        whenever(bookRepository.findById(bookId)).thenReturn(book)

        // When
        deleteBookUseCase.execute(command)

        // Then
        verify(bookRepository).findById(bookId)
        verify(bookRepository).deleteById(bookId)
    }

    @Test
    fun `should throw exception when book not found`() {
        // Given
        val command = DeleteBookUseCase.Command(UUID.randomUUID().toString())
        val bookId = BookId.fromString(command.bookId)

        whenever(bookRepository.findById(bookId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                deleteBookUseCase.execute(command)
            }
        assertEquals("Book with id ${command.bookId} not found", exception.message)
        assertEquals("BOOK_NOT_FOUND", exception.subType)
        verify(bookRepository).findById(bookId)
    }
}
