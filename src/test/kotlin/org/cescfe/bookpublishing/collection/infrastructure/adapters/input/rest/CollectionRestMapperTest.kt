package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper.CollectionRestMapper
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CollectionRestMapperTest {
    private val mapper = CollectionRestMapper()

    @Test
    fun `should map collection to DTO successfully`() {
        // Given
        val collection = CollectionObjectMother.createWithAllFields()

        // When
        val result = mapper.toDto(collection)

        // Then
        assertEquals(collection.id.value, result.id)
        assertEquals(collection.name.value, result.name)
        assertEquals(collection.readingLevel?.name, result.readingLevel?.name)
        assertEquals(collection.primaryLanguage?.name, result.primaryLanguage?.name)
        assertEquals(
            collection.secondaryLanguages?.value?.map { it.name },
            result.secondaryLanguages?.map { it.name },
        )
        assertEquals(collection.primaryGenre?.name, result.primaryGenre?.name)
        assertEquals(
            collection.secondaryGenres?.value?.map { it.name },
            result.secondaryGenres?.map { it.name },
        )
    }

    @Test
    fun `should map collection with minimal fields to DTO`() {
        // Given
        val collection = CollectionObjectMother.createMinimal()

        // When
        val result = mapper.toDto(collection)

        // Then
        assertNotNull(result)
        assertEquals(collection.id.value, result.id)
        assertEquals(collection.name.value, result.name)
        assertNull(result.readingLevel)
        assertNull(result.primaryLanguage)
        assertNull(result.secondaryLanguages)
        assertNull(result.primaryGenre)
        assertNull(result.secondaryGenres)
        assertNull(result.createdAt)
        assertNull(result.createdBy)
        assertNull(result.updatedAt)
        assertNull(result.updatedBy)
    }
}
