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
        assertEquals("Collection with id ${command.collectionId} not found", exception.message)
        assertEquals("COLLECTION_NOT_FOUND", exception.subType)
        verify(collectionRepository).findById(collectionId)
    }
}
