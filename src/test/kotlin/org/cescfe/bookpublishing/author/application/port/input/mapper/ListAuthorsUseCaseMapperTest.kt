package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.objectMothers.AuthorSummaryObjectMother
import kotlin.test.Test
import kotlin.test.assertEquals

class ListAuthorsUseCaseMapperTest {
    private val mapper = ListAuthorsUseCaseMapper()

    @Test
    fun `should map to paginated result correctly`() {
        // Given
        val authors =
            listOf(
                AuthorSummaryObjectMother.createFirstAuthorSummary(),
                AuthorSummaryObjectMother.createSecondAuthorSummary(),
            )
        val totalCount = 2L
        val page = 1
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(authors, totalCount, page, limit)

        // Then
        assertEquals(authors, result.data)
        assertEquals(2, result.meta.total)
        assertEquals(1, result.meta.page)
        assertEquals(2, result.meta.limit)
        assertEquals(1, result.meta.totalPages)
    }

    @Test
    fun `should calculate total pages correctly for multiple pages`() {
        // Given
        val authors = listOf(AuthorSummaryObjectMother.createFirstAuthorSummary())
        val totalCount = 5L
        val page = 2
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(authors, totalCount, page, limit)

        // Then
        assertEquals(5, result.meta.total)
        assertEquals(2, result.meta.page)
        assertEquals(2, result.meta.limit)
        assertEquals(3, result.meta.totalPages) // ceil(5/2) = 3
    }

    @Test
    fun `should handle empty result correctly`() {
        // Given
        val authors = emptyList<AuthorSummary>()
        val totalCount = 0L
        val page = 1
        val limit = 10

        // When
        val result = mapper.toPaginatedResult(authors, totalCount, page, limit)

        // Then
        assertEquals(emptyList(), result.data)
        assertEquals(0, result.meta.total)
        assertEquals(1, result.meta.page)
        assertEquals(10, result.meta.limit)
        assertEquals(0, result.meta.totalPages)
    }

    @Test
    fun `should convert Long to Int correctly`() {
        // Given
        val authors = emptyList<AuthorSummary>()
        val totalCount = 2147483647L
        val page = 1
        val limit = 1

        // When
        val result = mapper.toPaginatedResult(authors, totalCount, page, limit)

        // Then
        assertEquals(2147483647, result.meta.total)
    }
}
