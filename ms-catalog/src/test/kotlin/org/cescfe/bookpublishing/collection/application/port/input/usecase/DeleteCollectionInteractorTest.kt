package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.DeleteCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

class DeleteCollectionInteractorTest {
    private val collectionRepository: CollectionRepository = mock()
    private val deleteCollectionUseCase = DeleteCollectionInteractor(collectionRepository)

    companion object {
        private const val EXPECTED_ERROR_MESSAGE = "Collection with id %s not found"
        private const val EXPECTED_ERROR_SUBTYPE = "COLLECTION_NOT_FOUND"
    }

    @Test
    fun `should delete collection successfully`() {
        // Given
        val command = DeleteCollectionUseCase.Command(UUID.randomUUID().toString())
        val collectionId = CollectionId.fromString(command.collectionId)
        val collection = CollectionObjectMother.create()

        whenever(collectionRepository.findById(collectionId)).thenReturn(collection)

        // When
        deleteCollectionUseCase.execute(command)

        // Then
        verify(collectionRepository).findById(collectionId)
        verify(collectionRepository).deleteById(collectionId)
    }

    @Test
    fun `should throw exception when collection not found`() {
        // Given
        val command = DeleteCollectionUseCase.Command(UUID.randomUUID().toString())
        val collectionId = CollectionId.fromString(command.collectionId)

        whenever(collectionRepository.findById(collectionId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                deleteCollectionUseCase.execute(command)
            }
        assertEquals(String.format(EXPECTED_ERROR_MESSAGE, command.collectionId), exception.message)
        assertEquals(EXPECTED_ERROR_SUBTYPE, exception.subType)
        verify(collectionRepository).findById(collectionId)
    }
}
