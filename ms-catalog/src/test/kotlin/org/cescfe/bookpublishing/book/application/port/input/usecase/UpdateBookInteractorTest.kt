package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.mapper.UpdateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.domain.service.BookDomainService
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.book.objectMothers.UpdateBookCommandObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateBookInteractorTest {
    private val bookRepository = mock<BookRepository>()
    private val mapper = mock<UpdateBookUseCaseMapper>()
    private val bookDomainService = mock<BookDomainService>()
    private val updateBookUseCase = UpdateBookInteractor(bookRepository, mapper, bookDomainService)

    companion object {
        private const val EXISTING_ISBN = "9781234567890"
        private const val EXISTING_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
        private const val BOOK_NOT_FOUND_ERROR_MESSAGE = "Book with id $EXISTING_ID not found"
        private const val BOOK_NOT_FOUND_ERROR_SUBTYPE = "BOOK_NOT_FOUND"
    }

    @Test
    fun `should update book successfully`() {
        // Given
        val command = UpdateBookCommandObjectMother.create()
        val bookId = BookId.fromString(EXISTING_ID)
        val existingBook = BookObjectMother.createWithAllFields()
        val updatedBook =
            BookObjectMother.create(
                id = bookId.value,
                title = "Updated Title",
            )

        whenever(bookRepository.findById(bookId)).thenReturn(existingBook)
        whenever(mapper.toDomain(command, existingBook)).thenReturn(updatedBook)
        whenever(bookRepository.save(updatedBook)).thenReturn(updatedBook)

        // When
        val result = updateBookUseCase.execute(EXISTING_ID, command)

        // Then
        assertEquals(updatedBook, result)
        verify(bookRepository).findById(bookId)
        verify(bookDomainService).ensureIsbnUniquenessForUpdate(command.isbn, EXISTING_ID)
        verify(mapper).toDomain(command, existingBook)
        verify(bookRepository).save(updatedBook)
    }

    @Test
    fun `should throw exception when book not found`() {
        // Given
        val command = UpdateBookCommandObjectMother.create()
        val bookId = BookId.fromString(EXISTING_ID)

        whenever(bookRepository.findById(bookId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                updateBookUseCase.execute(EXISTING_ID, command)
            }
        assertEquals(BOOK_NOT_FOUND_ERROR_MESSAGE, exception.message)
        assertEquals(BOOK_NOT_FOUND_ERROR_SUBTYPE, exception.subType)
        verify(bookDomainService, never()).ensureIsbnUniquenessForUpdate(command.isbn, EXISTING_ID)
        verify(bookRepository).findById(bookId)
    }

    @Test
    fun `should throw exception when isbn already exists for another book`() {
        // Given
        val command =
            UpdateBookCommandObjectMother.create(
                isbn = EXISTING_ISBN,
            )
        val bookId = BookId.fromString(EXISTING_ID)
        val existingBook = BookObjectMother.createWithAllFields()

        whenever(bookRepository.findById(bookId)).thenReturn(existingBook)
        whenever(bookDomainService.ensureIsbnUniquenessForUpdate(EXISTING_ISBN, EXISTING_ID))
            .thenThrow(BookDomainException.isbnAlreadyExists(EXISTING_ISBN))

        // When & Then
        assertThrows<BookDomainException> {
            updateBookUseCase.execute(EXISTING_ID, command)
        }
        verify(bookRepository).findById(bookId)
        verify(bookDomainService).ensureIsbnUniquenessForUpdate(EXISTING_ISBN, EXISTING_ID)
    }
}
