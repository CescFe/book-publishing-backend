package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.mapper.CreateBookUseCaseMapper
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.domain.port.BookRepository
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
    private val bookRepository = mock<BookRepository>()
    private val mapper = mock<CreateBookUseCaseMapper>()
    private val createBookUseCase = CreateBookInteractor(bookRepository, mapper)

    companion object {
        private const val EXPECTED_ERROR_MESSAGE = "Book title cannot be blank"
        private const val EXPECTED_ERROR_SUBTYPE = "TITLE_CANNOT_BE_BLANK"
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

        assertEquals(EXPECTED_ERROR_MESSAGE, exception.message)
        assertEquals(EXPECTED_ERROR_SUBTYPE, exception.subType)
        verify(mapper).toDomain(input)
        verify(bookRepository, never()).save(any())
    }
}
