package org.cescfe.bookpublishing.collection.application.port.input.usecase

import org.cescfe.bookpublishing.collection.application.port.input.mapper.CreateCollectionUseCaseMapper
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.domain.service.CollectionDomainService
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.collection.objectMothers.CreateCollectionCommandObjectMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateCollectionInteractorTest {
    private val collectionRepository = mock<CollectionRepository>()
    private val mapper = mock<CreateCollectionUseCaseMapper>()
    private val collectionDomainService = mock<CollectionDomainService>()
    private val createCollectionUseCase =
        CreateCollectionInteractor(collectionRepository, mapper, collectionDomainService)

    companion object {
        private const val COLLECTION_NAME = "Collection name"
        private const val NAME_BLANK_ERROR_MESSAGE = "Collection name cannot be blank"
        private const val NAME_BLANK_ERROR_SUBTYPE = "NAME_CANNOT_BE_BLANK"
        private const val NAME_EXISTS_ERROR_MESSAGE = "Collection with name '$COLLECTION_NAME' already exists"
        private const val NAME_EXISTS_ERROR_SUBTYPE = "NAME_ALREADY_EXISTS"
    }

    @Test
    fun `should create collection successfully`() {
        // Given
        val input = CreateCollectionCommandObjectMother.createWithAllFields()
        val expectedCollection = CollectionObjectMother.createWithAllFields()

        whenever(mapper.toDomain(input)).thenReturn(expectedCollection)
        whenever(collectionRepository.save(any())).thenReturn(expectedCollection)

        // When
        val result = createCollectionUseCase.execute(input)

        // Then
        assertEquals(expectedCollection, result)

        verify(collectionDomainService).ensureNameUniqueness(input.name)
        verify(mapper).toDomain(input)
        verify(collectionRepository).save(any())
    }

    @Test
    fun `should create collection without optional fields`() {
        // Given
        val input = CreateCollectionCommandObjectMother.createMinimal()
        val expectedCollection = CollectionObjectMother.createMinimal()

        whenever(mapper.toDomain(input)).thenReturn(expectedCollection)
        whenever(collectionRepository.save(any())).thenReturn(expectedCollection)

        // When
        val result = createCollectionUseCase.execute(input)

        // Then
        assertEquals(expectedCollection, result)

        verify(collectionDomainService).ensureNameUniqueness(input.name)
        verify(mapper).toDomain(input)
        verify(collectionRepository).save(expectedCollection)
    }

    @Test
    fun `should propagate domain exceptions when mapper throws`() {
        // Given
        val input = CreateCollectionCommandObjectMother.create(name = "")

        whenever(mapper.toDomain(input))
            .thenThrow(CollectionDomainException.nameCannotBeBlank())

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                createCollectionUseCase.execute(input)
            }

        assertEquals(NAME_BLANK_ERROR_MESSAGE, exception.message)
        assertEquals(NAME_BLANK_ERROR_SUBTYPE, exception.subType)
        verify(collectionDomainService).ensureNameUniqueness(input.name)
        verify(mapper).toDomain(input)
        verify(collectionRepository, never()).save(any())
    }

    @Test
    fun `should throw exception when collection name already exists`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                name = COLLECTION_NAME,
            )

        whenever(collectionDomainService.ensureNameUniqueness(COLLECTION_NAME))
            .thenThrow(CollectionDomainException.nameAlreadyExists(COLLECTION_NAME))

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                createCollectionUseCase.execute(input)
            }

        assertEquals(NAME_EXISTS_ERROR_MESSAGE, exception.message)
        assertEquals(NAME_EXISTS_ERROR_SUBTYPE, exception.subType)
        verify(collectionDomainService).ensureNameUniqueness(COLLECTION_NAME)
        verify(mapper, never()).toDomain(any())
        verify(collectionRepository, never()).save(any())
    }
}
