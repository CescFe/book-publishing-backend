package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.objectMothers.CollectionSummaryObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ListCollectionsUseCaseMapperTest {
    private val mapper = ListCollectionsUseCaseMapper()

    @Test
    fun `should map to paginated result correctly`() {
        // Given
        val collections =
            listOf(
                CollectionSummaryObjectMother.createFirstCollectionSummary(),
                CollectionSummaryObjectMother.createSecondCollectionSummary(),
            )
        val totalCount = 2L
        val page = 1
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(collections, totalCount, page, limit)

        // Then
        assertEquals(collections, result.data)
        assertEquals(2, result.metadata.total)
        assertEquals(1, result.metadata.page)
        assertEquals(2, result.metadata.limit)
        assertEquals(1, result.metadata.totalPages)
    }

    @Test
    fun `should calculate total pages correctly for multiple pages`() {
        // Given
        val collections = listOf(CollectionSummaryObjectMother.createFirstCollectionSummary())
        val totalCount = 5L
        val page = 2
        val limit = 2

        // When
        val result = mapper.toPaginatedResult(collections, totalCount, page, limit)

        // Then
        assertEquals(5, result.metadata.total)
        assertEquals(2, result.metadata.page)
        assertEquals(2, result.metadata.limit)
        assertEquals(3, result.metadata.totalPages)
    }

    @Test
    fun `should handle empty result correctly`() {
        // Given
        val collections = emptyList<CollectionSummary>()
        val totalCount = 0L
        val page = 1
        val limit = 10

        // When
        val result = mapper.toPaginatedResult(collections, totalCount, page, limit)

        // Then
        assertEquals(collections, result.data)
        assertEquals(0, result.metadata.total)
        assertEquals(1, result.metadata.page)
        assertEquals(10, result.metadata.limit)
        assertEquals(0, result.metadata.totalPages)
    }

    @Test
    fun `should map to non paginated result correctly`() {
        // Given
        val collections =
            listOf(
                CollectionSummaryObjectMother.createFirstCollectionSummary(),
                CollectionSummaryObjectMother.createSecondCollectionSummary(),
            )
        val totalCount = 2L

        // When
        val result = mapper.toNonPaginatedResult(collections, totalCount)

        // Then
        assertEquals(collections, result.data)
        assertEquals(2, result.metadata.total)
    }

    @Test
    fun `should handle empty non paginated result correctly`() {
        // Given
        val collections = emptyList<CollectionSummary>()
        val totalCount = 0L

        // When
        val result = mapper.toNonPaginatedResult(collections, totalCount)

        // Then
        assertEquals(collections, result.data)
        assertEquals(0, result.metadata.total)
    }
}
