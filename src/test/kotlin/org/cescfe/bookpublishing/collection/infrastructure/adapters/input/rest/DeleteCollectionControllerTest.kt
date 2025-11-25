package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.application.port.input.DeleteCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteCollectionControllerTest {
    private lateinit var deleteCollectionUseCase: DeleteCollectionUseCase
    private lateinit var deleteCollectionController: DeleteCollectionController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Collection with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "COLLECTION_NOT_FOUND"
        private const val NON_EXISTENT_COLLECTION_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        deleteCollectionUseCase = Mockito.mock()
        deleteCollectionController = DeleteCollectionController(deleteCollectionUseCase)
    }

    @Test
    fun `should throw CollectionDomainException when deleting non-existent collection`() {
        // Given
        whenever(
            deleteCollectionUseCase.execute(DeleteCollectionUseCase.Command(NON_EXISTENT_COLLECTION_ID)),
        ).thenThrow(
            CollectionDomainException.collectionNotFound(NON_EXISTENT_COLLECTION_ID),
        )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                deleteCollectionController.deleteCollection(UUID.fromString(NON_EXISTENT_COLLECTION_ID))
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, NON_EXISTENT_COLLECTION_ID), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }
}
