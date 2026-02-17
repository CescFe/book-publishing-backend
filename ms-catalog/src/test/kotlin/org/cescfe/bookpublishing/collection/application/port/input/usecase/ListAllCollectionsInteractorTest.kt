package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.mapper.ListCollectionsUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class ListAllCollectionsInteractorTest {
    private val collectionRepository: CollectionRepository = mock()
    private val mapper: ListCollectionsUseCaseMapper = mock()
    private val listAllCollectionsUseCase = ListAllCollectionsInteractor(collectionRepository, mapper)

    @Test
    fun `should return non paginated result when collections found`() {
        // Given
        val collections =
            listOf(
                CollectionSummaryObjectMother.createFirstCollectionSummary(),
                CollectionSummaryObjectMother.createSecondCollectionSummary(),
            )
        val totalCount = 2L
        val expectedResult =
            NonPaginatedResult(
                data = collections,
                metadata =
                    NonPaginationMeta(
                        total = 2,
                    ),
            )

        whenever(collectionRepository.findAllSummary()).thenReturn(collections)
        whenever(collectionRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(collections, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllCollectionsUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(collectionRepository).findAllSummary()
        verify(collectionRepository).countAll()
        verify(mapper).toNonPaginatedResult(collections, totalCount)
    }

    @Test
    fun `should return empty non paginated result when no collections found`() {
        // Given
        val emptyCollections = emptyList<CollectionSummary>()
        val totalCount = 0L
        val expectedResult =
            NonPaginatedResult(
                data = emptyCollections,
                metadata =
                    NonPaginationMeta(
                        total = 0,
                    ),
            )

        whenever(collectionRepository.findAllSummary()).thenReturn(emptyCollections)
        whenever(collectionRepository.countAll()).thenReturn(totalCount)
        whenever(mapper.toNonPaginatedResult(emptyCollections, totalCount)).thenReturn(expectedResult)

        // When
        val result = listAllCollectionsUseCase.execute()

        // Then
        assertEquals(expectedResult, result)
        verify(collectionRepository).findAllSummary()
        verify(collectionRepository).countAll()
        verify(mapper).toNonPaginatedResult(emptyCollections, totalCount)
    }
}
