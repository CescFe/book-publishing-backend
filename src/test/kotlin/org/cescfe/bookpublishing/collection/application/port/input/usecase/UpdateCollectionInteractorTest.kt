package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.mapper.UpdateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.collection.objectMothers.UpdateCollectionCommandObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateCollectionInteractorTest {
    private val collectionRepository = mock<CollectionRepository>()
    private val mapper = mock<UpdateCollectionUseCaseMapper>()
    private val updateCollectionUseCase = UpdateCollectionInteractor(collectionRepository, mapper)

    companion object {
        private const val EXISTING_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @Test
    fun `should update collection successfully`() {
        // Given
        val command = UpdateCollectionCommandObjectMother.create()
        val collectionId = CollectionId.fromString(EXISTING_ID)
        val existingCollection = CollectionObjectMother.createWithAllFields()
        val updatedCollection =
            CollectionObjectMother.create(
                id = collectionId.value,
                name = "Updated Collection Name",
            )

        whenever(collectionRepository.findById(collectionId)).thenReturn(existingCollection)
        whenever(mapper.toDomain(command, existingCollection)).thenReturn(updatedCollection)
        whenever(collectionRepository.save(updatedCollection)).thenReturn(updatedCollection)

        // When
        val result = updateCollectionUseCase.execute(EXISTING_ID, command)

        // Then
        assertEquals(updatedCollection, result)
        verify(collectionRepository).findById(collectionId)
        verify(mapper).toDomain(command, existingCollection)
        verify(collectionRepository).save(updatedCollection)
    }

    @Test
    fun `should throw exception when collection not found`() {
        val command = UpdateCollectionCommandObjectMother.create()
        val collectionId = CollectionId.fromString(EXISTING_ID)

        whenever(collectionRepository.findById(collectionId)).thenReturn(null)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                updateCollectionUseCase.execute(EXISTING_ID, command)
            }
        assertEquals("Collection with id $EXISTING_ID not found", exception.message)
        assertEquals("COLLECTION_NOT_FOUND", exception.subType)
        verify(collectionRepository).findById(collectionId)
    }
}
