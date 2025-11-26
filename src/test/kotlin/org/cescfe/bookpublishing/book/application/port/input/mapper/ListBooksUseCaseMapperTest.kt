package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.objectMothers.BookSummaryObjectMother
import org.junit.Test
import kotlin.test.assertEquals

class ListBooksUseCaseMapperTest {
    private val mapper = ListBooksUseCaseMapper()

    @Test
    fun `should map to paginated result correctly`() {
        // Given
        val books =
            listOf(
                BookSummaryObjectMother.createFirstBookSummary(),
                BookSummaryObjectMother.createSecondBookSummary(),
            )
        val totalCount = 2L
        val page = 1
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(books, totalCount, page, limit)

        // Then
        assertEquals(books, result.data)
        assertEquals(2, result.metadata.total)
        assertEquals(1, result.metadata.page)
        assertEquals(2, result.metadata.limit)
        assertEquals(1, result.metadata.totalPages)
    }

    @Test
    fun `should calculate total pages correctly for multiple pages`() {
        // Given
        val books = listOf(BookSummaryObjectMother.createFirstBookSummary())
        val totalCount = 5L
        val page = 2
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(books, totalCount, page, limit)

        // Then
        assertEquals(5, result.metadata.total)
        assertEquals(2, result.metadata.page)
        assertEquals(2, result.metadata.limit)
        assertEquals(3, result.metadata.totalPages)
    }

    @Test
    fun `should handle empty result correctly`() {
        // Given
        val books = emptyList<BookSummary>()
        val totalCount = 0L
        val page = 1
        val limit = 10

        // When
        val result = mapper.toPaginatedResult(books, totalCount, page, limit)

        // Then
        assertEquals(books, result.data)
        assertEquals(0, result.metadata.total)
        assertEquals(1, result.metadata.page)
        assertEquals(10, result.metadata.limit)
        assertEquals(0, result.metadata.totalPages)
    }

    @Test
    fun `should calculate total pages correctly when total count is exactly divisible by limit`() {
        // Given
        val books = listOf(BookSummaryObjectMother.createFirstBookSummary())
        val totalCount = 4L
        val page = 1
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(books, totalCount, page, limit)

        // Then
        assertEquals(4, result.metadata.total)
        assertEquals(1, result.metadata.page)
        assertEquals(2, result.metadata.limit)
        assertEquals(2, result.metadata.totalPages)
    }
}
