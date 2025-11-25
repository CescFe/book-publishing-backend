package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.GetCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCollectionInteractorTest {
    private val collectionRepository: CollectionRepository = mock()
    private val getCollectionUseCase = GetCollectionInteractor(collectionRepository)

    companion object {
        private const val NOT_FOUND_MESSAGE = "Collection with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "COLLECTION_NOT_FOUND"
        private const val INVALID_UUID = "invalid-uuid"
        private const val INVALID_UUID_MESSAGE = "Collection id '%s' has invalid format. Expected a valid UUID"
        private const val INVALID_UUID_SUBTYPE = "COLLECTION_ID_INVALID_FORMAT"
    }

    @Test
    fun `should return collection when found`() {
        // Given
        val query = GetCollectionUseCase.Query(collectionId = UUID.randomUUID().toString())
        val collectionId = CollectionId.fromString(query.collectionId)
        val expectedCollection = CollectionObjectMother.createWithAllFields()

        whenever(collectionRepository.findById(collectionId)).thenReturn(expectedCollection)

        // When
        val result = getCollectionUseCase.execute(query)

        // Then
        assertEquals(expectedCollection, result)
        verify(collectionRepository).findById(collectionId)
    }

    @Test
    fun `should return CollectionDomainException when collection not found`() {
        // Given
        val query = GetCollectionUseCase.Query(collectionId = UUID.randomUUID().toString())
        val collectionId = CollectionId.fromString(query.collectionId)

        whenever(collectionRepository.findById(collectionId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                getCollectionUseCase.execute(query)
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, query.collectionId), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
        verify(collectionRepository).findById(collectionId)
    }

    @Test
    fun `should throw exception when collectionId is invalid`() {
        // Given
        val query = GetCollectionUseCase.Query(collectionId = INVALID_UUID)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                getCollectionUseCase.execute(query)
            }
        assertEquals(String.format(INVALID_UUID_MESSAGE, query.collectionId), exception.message)
        assertEquals(INVALID_UUID_SUBTYPE, exception.subType)
    }
}
