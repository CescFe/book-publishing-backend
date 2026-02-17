package org.cescfe.bookpublishing.collection.domain.model

import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CollectionTest {
    @Test
    fun `should create collection with valid data`() {
        // Given
        val id = CollectionId.generate()
        val name = CollectionName("Fantasy Classics")
        val readingLevel = ReadingLevel.ADULT
        val primaryLanguage = Language.ENGLISH
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryGenre = Genre.FANTASY
        val secondaryGenres = SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))

        // When
        val collection =
            Collection(
                id = id,
                name = name,
                readingLevel = readingLevel,
                primaryLanguage = primaryLanguage,
                secondaryLanguages = secondaryLanguages,
                primaryGenre = primaryGenre,
                secondaryGenres = secondaryGenres,
            )

        // Then
        assertEquals(id, collection.id)
        assertEquals(name, collection.name)
        assertEquals(readingLevel, collection.readingLevel)
        assertEquals(primaryLanguage, collection.primaryLanguage)
        assertEquals(secondaryLanguages, collection.secondaryLanguages)
        assertEquals(primaryGenre, collection.primaryGenre)
        assertEquals(secondaryGenres, collection.secondaryGenres)
    }

    @Test
    fun `should create collection with minimal required data`() {
        // Given
        val id = CollectionId.generate()
        val name = CollectionName("Fantasy Classics")

        // When
        val collection = Collection(id = id, name = name)

        // Then
        assertEquals(id, collection.id)
        assertEquals(name, collection.name)
        assertEquals(null, collection.readingLevel)
        assertEquals(null, collection.primaryLanguage)
        assertEquals(null, collection.secondaryLanguages)
        assertEquals(null, collection.primaryGenre)
        assertEquals(null, collection.secondaryGenres)
    }
}
