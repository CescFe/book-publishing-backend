package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.mapper.CreateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.domain.service.BookDomainService
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.book.objectMothers.CreateBookCommandObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateBookInteractorTest {
    private val bookRepository: BookRepository = mock()
    private val mapper: CreateBookUseCaseMapper = mock()
    private val bookDomainService: BookDomainService = mock()
    private val createBookUseCase = CreateBookInteractor(bookRepository, mapper, bookDomainService)

    companion object {
        private const val ISBN = "9780007141326"
        private const val TITLE_BLANK_ERROR_MESSAGE = "Book title cannot be blank"
        private const val TITLE_BLANK_ERROR_SUBTYPE = "TITLE_CANNOT_BE_BLANK"
        private const val ISBN_EXISTS_ERROR_MESSAGE = "Book with ISBN '$ISBN' already exists"
        private const val ISBN_EXISTS_ERROR_SUBTYPE = "ISBN_ALREADY_EXISTS"
    }

    @Test
    fun `should create book successfully`() {
        // Given
        val input = CreateBookCommandObjectMother.createWithAllFields()
        val expectedBook = BookObjectMother.createWithAllFields()

        whenever(mapper.toDomain(input)).thenReturn(expectedBook)
        whenever(bookRepository.save(any())).thenReturn(expectedBook)

        // When
        val result = createBookUseCase.execute(input)

        // Then
        assertEquals(expectedBook, result)

        verify(bookDomainService).ensureIsbnUniqueness(ISBN)
        verify(mapper).toDomain(input)
        verify(bookRepository).save(any())
    }

    @Test
    fun `should create book without optional fields`() {
        // Given
        val input = CreateBookCommandObjectMother.createMinimal()
        val expectedBook = BookObjectMother.createMinimal()

        whenever(mapper.toDomain(input)).thenReturn(expectedBook)
        whenever(bookRepository.save(any())).thenReturn(expectedBook)

        // When
        val result = createBookUseCase.execute(input)

        // Then
        assertEquals(expectedBook, result)

        verify(bookDomainService).ensureIsbnUniqueness(null)
        verify(mapper).toDomain(input)
        verify(bookRepository).save(expectedBook)
    }

    @Test
    fun `should propagate domain exceptions when mapper throws`() {
        // Given
        val input = CreateBookCommandObjectMother.create(title = "")

        whenever(mapper.toDomain(input))
            .thenThrow(BookDomainException.titleCannotBeBlank())

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                createBookUseCase.execute(input)
            }

        assertEquals(TITLE_BLANK_ERROR_MESSAGE, exception.message)
        assertEquals(TITLE_BLANK_ERROR_SUBTYPE, exception.subType)
        verify(mapper).toDomain(input)
        verify(bookRepository, never()).save(any())
    }

    @Test
    fun `should throw exception when ISBN already exists`() {
        // Given
        val input = CreateBookCommandObjectMother.createWithIsbn(ISBN)

        whenever(bookDomainService.ensureIsbnUniqueness(ISBN))
            .thenThrow(BookDomainException.isbnAlreadyExists(ISBN))

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                createBookUseCase.execute(input)
            }

        assertEquals(ISBN_EXISTS_ERROR_MESSAGE, exception.message)
        assertEquals(ISBN_EXISTS_ERROR_SUBTYPE, exception.subType)
        verify(bookDomainService).ensureIsbnUniqueness(ISBN)
        verify(mapper, never()).toDomain(any())
        verify(bookRepository, never()).save(any())
    }
}
