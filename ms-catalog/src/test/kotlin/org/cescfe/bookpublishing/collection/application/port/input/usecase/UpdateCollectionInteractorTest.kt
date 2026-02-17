package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.mapper.UpdateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.domain.service.CollectionDomainService
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.collection.objectMothers.UpdateCollectionCommandObjectMother
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateCollectionInteractorTest {
    private val collectionRepository = mock<CollectionRepository>()
    private val mapper = mock<UpdateCollectionUseCaseMapper>()
    private val collectionDomainService = mock<CollectionDomainService>()
    private val updateCollectionUseCase =
        UpdateCollectionInteractor(collectionRepository, mapper, collectionDomainService)

    companion object {
        private const val EXISTING_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
        private const val EXISTING_NAME = "Existing Collection Name"
        private const val COLLECTION_NOT_FOUND_ERROR_MESSAGE = "Collection with id $EXISTING_ID not found"
        private const val COLLECTION_NOT_FOUND_ERROR_SUBTYPE = "COLLECTION_NOT_FOUND"
        private const val NAME_EXISTS_ERROR_MESSAGE = "Collection with name '$EXISTING_NAME' already exists"
        private const val NAME_EXISTS_ERROR_SUBTYPE = "NAME_ALREADY_EXISTS"
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
        verify(collectionDomainService).ensureNameUniquenessForUpdate(command.name, EXISTING_ID)
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
        assertEquals(COLLECTION_NOT_FOUND_ERROR_MESSAGE, exception.message)
        assertEquals(COLLECTION_NOT_FOUND_ERROR_SUBTYPE, exception.subType)
        verify(collectionDomainService, never()).ensureNameUniquenessForUpdate(command.name, EXISTING_ID)
        verify(collectionRepository).findById(collectionId)
    }

    @Test
    fun `should throw exception when collection name already exists for another collection`() {
        // Given
        val command =
            UpdateCollectionCommandObjectMother.create(
                name = EXISTING_NAME,
            )
        val collectionId = CollectionId.fromString(EXISTING_ID)
        val existingCollection = CollectionObjectMother.createWithAllFields()

        whenever(collectionRepository.findById(collectionId)).thenReturn(existingCollection)
        whenever(collectionDomainService.ensureNameUniquenessForUpdate(command.name, EXISTING_ID))
            .thenThrow(CollectionDomainException.nameAlreadyExists(command.name))

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                updateCollectionUseCase.execute(EXISTING_ID, command)
            }

        assertEquals(NAME_EXISTS_ERROR_MESSAGE, exception.message)
        assertEquals(NAME_EXISTS_ERROR_SUBTYPE, exception.subType)
        verify(collectionRepository).findById(collectionId)
        verify(collectionDomainService).ensureNameUniquenessForUpdate(command.name, EXISTING_ID)
    }
}
