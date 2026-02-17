package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.ListCollectionsPaginatedUseCase
import org.cescfe.bookpublishing.collection.application.port.input.mapper.ListCollectionsUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.PaginationMeta
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ListCollectionsPaginatedInteractorTest {
    private val collectionRepository: CollectionRepository = mock()
    private val mapper: ListCollectionsUseCaseMapper = mock()
    private val listCollectionsUseCase = ListCollectionsPaginatedInteractor(collectionRepository, mapper)

    @Test
    fun `should return paginated result when collections found`() {
        // Given
        val query = ListCollectionsPaginatedUseCase.Query(page = 1, limit = 2)
        val collections =
            listOf(
                CollectionSummaryObjectMother.createFirstCollectionSummary(),
                CollectionSummaryObjectMother.createSecondCollectionSummary(),
            )
        val totalCount = 2L
        val expectedResult =
            PaginatedResult(
                data = collections,
                metadata =
                    PaginationMeta(
                        total = 2,
                        page = 1,
                        limit = 2,
                        totalPages = 1,
                    ),
            )

        whenever(collectionRepository.findAllSummary(query.page, query.limit)).thenReturn(collections)
        whenever(collectionRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(collections, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listCollectionsUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(collectionRepository).findAllSummary(query.page, query.limit)
        verify(collectionRepository).countAll()
        verify(mapper).toPaginatedResult(collections, totalCount, query.page, query.limit)
    }

    @Test
    fun `should return empty result when no collections found`() {
        // Given
        val query = ListCollectionsPaginatedUseCase.Query(page = 1, limit = 10)
        val collections = emptyList<CollectionSummary>()
        val totalCount = 0L
        val expectedResult =
            PaginatedResult(
                data = collections,
                metadata =
                    PaginationMeta(
                        total = 0,
                        page = 1,
                        limit = 10,
                        totalPages = 0,
                    ),
            )

        whenever(collectionRepository.findAllSummary(query.page, query.limit)).thenReturn(collections)
        whenever(collectionRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(collections, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listCollectionsUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(collectionRepository).findAllSummary(query.page, query.limit)
        verify(collectionRepository).countAll()
        verify(mapper).toPaginatedResult(collections, totalCount, query.page, query.limit)
    }

    @Test
    fun `should handle pagination correctly for multiple pages`() {
        // Given
        val query = ListCollectionsPaginatedUseCase.Query(page = 2, limit = 2)
        val collections = listOf(CollectionSummaryObjectMother.createFirstCollectionSummary())
        val totalCount = 5L
        val expectedResult =
            PaginatedResult(
                data = collections,
                metadata =
                    PaginationMeta(
                        total = 5,
                        page = 2,
                        limit = 2,
                        totalPages = 3,
                    ),
            )

        whenever(collectionRepository.findAllSummary(query.page, query.limit)).thenReturn(collections)
        whenever(collectionRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toPaginatedResult(collections, totalCount, query.page, query.limit))
            .thenReturn(expectedResult)

        // When
        val result = listCollectionsUseCase.execute(query)

        // Then
        assertEquals(expectedResult, result)
        verify(collectionRepository).findAllSummary(query.page, query.limit)
        verify(collectionRepository).countAll()
        verify(mapper).toPaginatedResult(collections, totalCount, query.page, query.limit)
    }
}
