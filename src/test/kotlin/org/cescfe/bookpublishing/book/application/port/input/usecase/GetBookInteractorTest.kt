package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.GetBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBookInteractorTest {
    private val bookRepository: BookRepository = mock()
    private val getBookUseCase = GetBookInteractor(bookRepository)

    companion object {
        private const val NOT_FOUND_MESSAGE = "Book with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "BOOK_NOT_FOUND"
        private const val INVALID_UUID = "invalid-uuid"
        private const val INVALID_UUID_MESSAGE = "Book id '%s' has invalid format. Expected a valid UUID"
        private const val INVALID_UUID_SUBTYPE = "BOOK_ID_INVALID_FORMAT"
    }

    @Test
    fun `should return book when found`() {
        // Given
        val query = GetBookUseCase.Query(bookId = UUID.randomUUID().toString())
        val bookId = BookId.fromString(query.bookId)
        val expectedBook = BookObjectMother.createWithAllFields()

        whenever(bookRepository.findById(bookId)).thenReturn(expectedBook)

        // When
        val result = getBookUseCase.execute(query)

        // Then
        assertEquals(expectedBook, result)
        verify(bookRepository).findById(bookId)
    }

    @Test
    fun `should return BookDomainException when book not found`() {
        // Given
        val query = GetBookUseCase.Query(bookId = UUID.randomUUID().toString())
        val bookId = BookId.fromString(query.bookId)

        whenever(bookRepository.findById(bookId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                getBookUseCase.execute(query)
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, query.bookId), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
        verify(bookRepository).findById(bookId)
    }

    @Test
    fun `should throw exception when bookId is invalid`() {
        // Given
        val query = GetBookUseCase.Query(bookId = INVALID_UUID)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                getBookUseCase.execute(query)
            }
        assertEquals(String.format(INVALID_UUID_MESSAGE, query.bookId), exception.message)
        assertEquals(INVALID_UUID_SUBTYPE, exception.subType)
    }
}
