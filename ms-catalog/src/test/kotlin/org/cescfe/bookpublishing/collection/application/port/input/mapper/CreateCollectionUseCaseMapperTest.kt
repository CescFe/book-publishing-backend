package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.objectMothers.CreateCollectionCommandObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CreateCollectionUseCaseMapperTest {
    private val mapper = CreateCollectionUseCaseMapper()

    @Test
    fun `should map input values to domain collection`() {
        // Given
        val input = CreateCollectionCommandObjectMother.createWithAllFields()

        // When
        val result = mapper.toDomain(input)

        // Then
        assertEquals(input.name, result.name.value)
        assertEquals(input.readingLevel, result.readingLevel)
        assertEquals(input.primaryLanguage, result.primaryLanguage)
        assertEquals(input.secondaryLanguages, result.secondaryLanguages!!.value)
        assertEquals(input.primaryGenre, result.primaryGenre)
        assertEquals(input.secondaryGenres, result.secondaryGenres!!.value)
    }

    @Test
    fun `should map input values with minimal fields`() {
        // Given
        val input = CreateCollectionCommandObjectMother.createMinimal()

        // When
        val result = mapper.toDomain(input)

        // Then
        assertEquals(input.name, result.name.value)
        assertNull(result.readingLevel)
        assertNull(result.primaryLanguage)
        assertNull(result.secondaryLanguages)
        assertNull(result.primaryGenre)
        assertNull(result.secondaryGenres)
    }
}
