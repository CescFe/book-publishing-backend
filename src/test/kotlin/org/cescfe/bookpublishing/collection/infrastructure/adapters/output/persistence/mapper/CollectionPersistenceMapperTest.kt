package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.collection.objectMothers.CollectionEntityObjectMother
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CollectionPersistenceMapperTest {
    private val collectionMapper = CollectionPersistenceMapper()

    @Test
    fun `fromDomain should map collection domain to entity correctly`() {
        // Given
        val collection = CollectionObjectMother.createFantasyClassics()

        // When
        val result = collectionMapper.fromDomain(collection)

        // Then
        assertEquals(collection.id.value, result.id)
        assertEquals(collection.name.value, result.name)
        assertEquals(collection.readingLevel, result.readingLevel)
        assertEquals(collection.primaryLanguage, result.primaryLanguage)
        assertEquals(collection.primaryGenre, result.primaryGenre)
        assertEquals(
            collection.secondaryLanguages!!.value,
            result.secondaryLanguages,
        )
        assertEquals(
            collection.secondaryGenres!!.value,
            result.secondaryGenres,
        )
    }

    @Test
    fun `fromDomain should map collection with minimal data correctly`() {
        // Given
        val collection = CollectionObjectMother.createMinimal()

        // When
        val result = collectionMapper.fromDomain(collection)

        // Then
        assertEquals(collection.id.value, result.id)
        assertEquals(collection.name.value, result.name)
        assertNull(result.readingLevel)
        assertNull(result.primaryLanguage)
        assertNull(result.primaryGenre)
        assertNull(result.secondaryLanguages)
        assertNull(result.secondaryGenres)
    }

    @Test
    fun `toDomain should map entity to collection domain correctly`() {
        // Given
        val entity = CollectionEntityObjectMother.createFantasyClassics()

        // When
        val result = collectionMapper.toDomain(entity)

        // Then
        assertEquals(CollectionId(entity.id), result.id)
        assertEquals(CollectionName(entity.name), result.name)
        assertEquals(entity.readingLevel, result.readingLevel)
        assertEquals(entity.primaryLanguage, result.primaryLanguage)
        assertEquals(entity.primaryGenre, result.primaryGenre)
        assertEquals(
            SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH)),
            result.secondaryLanguages,
        )
        assertEquals(
            SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION)),
            result.secondaryGenres,
        )
    }

    @Test
    fun `toDomain should map entity with minimal data correctly`() {
        // Given
        val entity = CollectionEntityObjectMother.createSimple()

        // When
        val result = collectionMapper.toDomain(entity)

        // Then
        assertEquals(CollectionId(entity.id), result.id)
        assertEquals(CollectionName(entity.name), result.name)
        assertNull(result.readingLevel)
        assertNull(result.primaryLanguage)
        assertNull(result.primaryGenre)
        assertNull(result.secondaryLanguages)
        assertNull(result.secondaryGenres)
    }

    @Test
    fun `toDomain should map entity with single secondary language correctly`() {
        // Given
        val entity =
            CollectionEntityObjectMother.create(
                name = "Test Collection",
                secondaryLanguages = listOf(Language.ENGLISH),
            )

        // When
        val result = collectionMapper.toDomain(entity)

        // Then
        assertEquals(
            SecondaryLanguages(listOf(Language.ENGLISH)),
            result.secondaryLanguages,
        )
    }

    @Test
    fun `toDomain should map entity with single secondary genre correctly`() {
        // Given
        val entity =
            CollectionEntityObjectMother.create(
                name = "Test Collection",
                secondaryGenres = listOf(Genre.FANTASY),
            )

        // When
        val result = collectionMapper.toDomain(entity)

        // Then
        assertEquals(
            SecondaryGenres(listOf(Genre.FANTASY)),
            result.secondaryGenres,
        )
    }
}
