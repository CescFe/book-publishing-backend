package org.cescfe.bookpublishing.book.application.port.input.usecase

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.book.application.port.input.ListBooksUseCase
import org.cescfe.bookpublishing.book.application.port.input.mapper.ListBooksUseCaseMapper
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.objectMothers.BookSummaryObjectMother
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class ListBooksInteractorTest {
    private val bookRepository: BookRepository = mock()
    private val mapper: ListBooksUseCaseMapper = mock()
    private val listBooksUseCase = ListBooksInteractor(bookRepository, mapper)

    @Test
    fun `should return paginated result when books found`() {
        // Given
        val query = ListBooksUseCase.Query(page = 1, limit = 2)
        val books =
            listOf(
                BookSummaryObjectMother.createFirstBookSummary(),
                BookSummaryObjectMother.createSecondBookSummary(),
            )
        val totalCount = 2L
        val expectedResult =
            PaginatedResult(
                data = books,
                metadata =
                    PaginationMeta(
                        total = 2,
                        page = 1,
                        limit = 2,
                        totalPages = 1,
                    ),
            )

        whenever(bookRepository.findAllSummary(query.page, query.limit)).thenReturn(books)
        whenever(bookRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(books, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listBooksUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(bookRepository).findAllSummary(query.page, query.limit)
        verify(bookRepository).countAll()
        verify(mapper).toPaginatedResult(books, totalCount, query.page, query.limit)
    }

    @Test
    fun `should return empty result when no books found`() {
        // Given
        val query = ListBooksUseCase.Query(page = 1, limit = 10)
        val books = emptyList<BookSummary>()
        val totalCount = 0L
        val expectedResult =
            PaginatedResult(
                data = books,
                metadata =
                    PaginationMeta(
                        total = 0,
                        page = 1,
                        limit = 10,
                        totalPages = 0,
                    ),
            )

        whenever(bookRepository.findAllSummary(query.page, query.limit)).thenReturn(books)
        whenever(bookRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(books, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listBooksUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(bookRepository).findAllSummary(query.page, query.limit)
        verify(bookRepository).countAll()
        verify(mapper).toPaginatedResult(books, totalCount, query.page, query.limit)
    }

    @Test
    fun `should handle pagination correctly for multiple pages`() {
        // Given
        val query = ListBooksUseCase.Query(page = 2, limit = 2)
        val books = listOf(BookSummaryObjectMother.createFirstBookSummary())
        val totalCount = 5L
        val expectedResult =
            PaginatedResult(
                data = books,
                metadata =
                    PaginationMeta(
                        total = 5,
                        page = 2,
                        limit = 2,
                        totalPages = 3,
                    ),
            )

        whenever(bookRepository.findAllSummary(query.page, query.limit)).thenReturn(books)
        whenever(bookRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(books, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listBooksUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(bookRepository).findAllSummary(query.page, query.limit)
        verify(bookRepository).countAll()
        verify(mapper).toPaginatedResult(books, totalCount, query.page, query.limit)
    }
}
