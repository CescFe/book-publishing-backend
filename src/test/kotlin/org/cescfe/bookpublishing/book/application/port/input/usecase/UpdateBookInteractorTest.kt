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
    }

    @Test
    fun `should update book successfully`() {
        // Given
        val input = UpdateBookCommandObjectMother.createWithTestId()
        val bookId = BookId.fromString(input.bookId)
        val existingBook = BookObjectMother.createWithAllFields()
        val updatedBook =
            BookObjectMother.create(
                id = bookId.value,
                title = "Updated Title",
            )

        whenever(bookRepository.findById(bookId)).thenReturn(existingBook)
        whenever(bookDomainService.ensureIsbnUniquenessForUpdate(input.isbn, input.bookId)).then { }
        whenever(mapper.toDomain(input, existingBook)).thenReturn(updatedBook)
        whenever(bookRepository.save(updatedBook)).thenReturn(updatedBook)

        // When
        val result = updateBookUseCase.execute(input)

        // Then
        assertEquals(updatedBook, result)
        verify(bookRepository).findById(bookId)
        verify(bookDomainService).ensureIsbnUniquenessForUpdate(input.isbn, input.bookId)
        verify(mapper).toDomain(input, existingBook)
        verify(bookRepository).save(updatedBook)
    }

    @Test
    fun `should throw exception when book not found`() {
        // Given
        val input = UpdateBookCommandObjectMother.createWithTestId()
        val bookId = BookId.fromString(input.bookId)

        whenever(bookRepository.findById(bookId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                updateBookUseCase.execute(input)
            }
        assertEquals("Book with id ${input.bookId} not found", exception.message)
        assertEquals("BOOK_NOT_FOUND", exception.subType)
        verify(bookRepository).findById(bookId)
    }

    @Test
    fun `should throw exception when isbn already exists for another book`() {
        // Given
        val input =
            UpdateBookCommandObjectMother.create(
                bookId = EXISTING_ID,
                isbn = EXISTING_ISBN,
            )
        val bookId = BookId.fromString(input.bookId)
        val existingBook = BookObjectMother.createWithAllFields()

        whenever(bookRepository.findById(bookId)).thenReturn(existingBook)
        whenever(bookDomainService.ensureIsbnUniquenessForUpdate(EXISTING_ISBN, input.bookId))
            .thenThrow(BookDomainException.isbnAlreadyExists(EXISTING_ISBN))

        // When & Then
        assertThrows<BookDomainException> {
            updateBookUseCase.execute(input)
        }
        verify(bookRepository).findById(bookId)
        verify(bookDomainService).ensureIsbnUniquenessForUpdate(EXISTING_ISBN, input.bookId)
    }
}
