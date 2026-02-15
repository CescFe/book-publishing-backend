package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.book.application.port.input.mapper.ListBooksUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.objectMothers.BookSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class ListAllBooksInteractorTest {
    private val bookRepository: BookRepository = mock()
    private val mapper: ListBooksUseCaseMapper = mock()
    private val listAllBooksUseCase = ListAllBooksInteractor(bookRepository, mapper)

    @Test
    fun `should return non paginated result when books found`() {
        // Given
        val books =
            listOf(
                BookSummaryObjectMother.createFirstBookSummary(),
                BookSummaryObjectMother.createSecondBookSummary(),
            )
        val totalCount = 2L
        val expectedResult =
            NonPaginatedResult(
                data = books,
                metadata =
                    NonPaginationMeta(
                        total = 2,
                    ),
            )

        whenever(bookRepository.findAllSummary()).thenReturn(books)
        whenever(bookRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(books, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllBooksUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(bookRepository).findAllSummary()
        verify(bookRepository).countAll()
        verify(mapper).toNonPaginatedResult(books, totalCount)
    }

    @Test
    fun `should return empty non paginated result when no books found`() {
        // Given
        val emptyBooks = emptyList<BookSummary>()
        val totalCount = 0L
        val expectedResult =
            NonPaginatedResult(
                data = emptyBooks,
                metadata =
                    NonPaginationMeta(
                        total = 0,
                    ),
            )

        whenever(bookRepository.findAllSummary()).thenReturn(emptyBooks)
        whenever(bookRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(emptyBooks, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllBooksUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(bookRepository).findAllSummary()
        verify(bookRepository).countAll()
        verify(mapper).toNonPaginatedResult(emptyBooks, totalCount)
    }
}
