package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateCollectionRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateCollectionControllerTest {
    private lateinit var updateCollectionUseCase: UpdateCollectionUseCase
    private lateinit var updateCollectionController: UpdateCollectionController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Collection with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "COLLECTION_NOT_FOUND"
        private const val NAME_CONFLICT_MESSAGE = "Collection with name '%s' already exists"
        private const val NAME_CONFLICT_SUBTYPE = "NAME_ALREADY_EXISTS"
        private const val NON_EXISTENT_COLLECTION_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
        private const val EXISTING_NAME = "Existing Collection Name"
    }

    @BeforeEach
    fun setup() {
        updateCollectionUseCase = mock()
        updateCollectionController = UpdateCollectionController(updateCollectionUseCase)
    }

    @Test
    fun `should throw CollectionDomainException when updating non-existent collection`() {
        // Given
        val collectionId = UUID.fromString(NON_EXISTENT_COLLECTION_ID)
        val requestDTO =
            UpdateCollectionRequestDTO(
                name = EXISTING_NAME,
            )

        whenever(
            updateCollectionUseCase.execute(
                any(),
                any(),
            ),
        ).thenThrow(
            CollectionDomainException.collectionNotFound(NON_EXISTENT_COLLECTION_ID),
        )

        // When & Then
        val exception =
            assertFailsWith<CollectionDomainException> {
                updateCollectionController.updateCollection(collectionId, requestDTO)
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, NON_EXISTENT_COLLECTION_ID), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when name already exists`() {
        // Given
        val collectionId = UUID.fromString(NON_EXISTENT_COLLECTION_ID)
        val requestDTO =
            UpdateCollectionRequestDTO(
                name = EXISTING_NAME,
            )

        whenever(
            updateCollectionUseCase.execute(
                any(),
                any(),
            ),
        ).thenThrow(
            CollectionDomainException.nameAlreadyExists(EXISTING_NAME),
        )

        // When & Then
        val exception =
            assertFailsWith<CollectionDomainException> {
                updateCollectionController.updateCollection(collectionId, requestDTO)
            }
        assertEquals(String.format(NAME_CONFLICT_MESSAGE, EXISTING_NAME), exception.message)
        assertEquals(NAME_CONFLICT_SUBTYPE, exception.subType)
    }
}
