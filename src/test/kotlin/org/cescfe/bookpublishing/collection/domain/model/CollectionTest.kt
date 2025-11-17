package org.cescfe.bookpublishing.collection.domain.model

import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
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

class CollectionNameTest {
    @Test
    fun `should create collection name with valid data`() {
        // When
        val name = CollectionName("Fantasy Classics")

        // Then
        assertEquals("Fantasy Classics", name.value)
    }

    @Test
    fun `should throw CollectionDomainException when name is blank`() {
        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                CollectionName("   ")
            }
        assertEquals("Collection name cannot be blank", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when name is too long`() {
        // Given
        val longName = "a".repeat(81)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                CollectionName(longName)
            }
        assertEquals("Collection name must be between 1 and 80 characters", exception.message)
    }

    @Test
    fun `should create collection name with exactly 80 characters`() {
        // Given
        val name80Chars = "a".repeat(80)

        // When
        val name = CollectionName(name80Chars)

        // Then
        assertEquals(80, name.value.length)
    }
}

class CollectionIdTest {
    @Test
    fun `should generate unique CollectionId`() {
        // When
        val id1 = CollectionId.generate()
        val id2 = CollectionId.generate()

        // Then
        assertEquals(UUID::class.java, id1.value.javaClass)
        assertEquals(UUID::class.java, id2.value.javaClass)
        assert(id1 != id2)
    }

    @Test
    fun `should create CollectionId from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val id = CollectionId.fromString(uuidString)

        // Then
        assertEquals(uuidString, id.value.toString())
    }
}

class SecondaryLanguagesTest {
    @Test
    fun `should create secondary languages with valid data`() {
        // When
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))

        // Then
        assertEquals(2, secondaryLanguages.value.size)
        assertEquals(Language.CATALAN, secondaryLanguages.value[0])
        assertEquals(Language.SPANISH, secondaryLanguages.value[1])
    }

    @Test
    fun `should create secondary languages with maximum allowed items`() {
        // When
        val secondaryLanguages =
            SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH, Language.ENGLISH))

        // Then
        assertEquals(3, secondaryLanguages.value.size)
    }

    @Test
    fun `should throw CollectionDomainException when secondary languages exceed maximum`() {
        // Given
        val tooManyLanguages =
            listOf(Language.CATALAN, Language.SPANISH, Language.ENGLISH, Language.VALENCIAN)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                SecondaryLanguages(tooManyLanguages)
            }
        assertEquals("Secondary languages cannot exceed 3 items", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when secondary languages contain duplicates`() {
        // Given
        val duplicatedLanguages = listOf(Language.CATALAN, Language.SPANISH, Language.CATALAN)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                SecondaryLanguages(duplicatedLanguages)
            }
        assertEquals("Secondary languages cannot contain duplicates", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when secondary languages contain primary language`() {
        // Given
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryLanguage = Language.CATALAN

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                secondaryLanguages.validateNotContainingPrimary(primaryLanguage)
            }
        assertEquals("Secondary languages cannot contain the primary language", exception.message)
    }

    @Test
    fun `should not throw when secondary languages do not contain primary language`() {
        // Given
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryLanguage = Language.ENGLISH

        // When & Then - should not throw
        secondaryLanguages.validateNotContainingPrimary(primaryLanguage)
    }
}

class SecondaryGenresTest {
    @Test
    fun `should create secondary genres with valid data`() {
        // When
        val secondaryGenres = SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))

        // Then
        assertEquals(2, secondaryGenres.value.size)
        assertEquals(Genre.ADVENTURE, secondaryGenres.value[0])
        assertEquals(Genre.HISTORICAL_FICTION, secondaryGenres.value[1])
    }

    @Test
    fun `should create secondary genres with maximum allowed items`() {
        // When
        val secondaryGenres =
            SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION, Genre.MYSTERY))

        // Then
        assertEquals(3, secondaryGenres.value.size)
    }

    @Test
    fun `should throw CollectionDomainException when secondary genres exceed maximum`() {
        // Given
        val tooManyGenres =
            listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION, Genre.MYSTERY, Genre.ROMANCE)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                SecondaryGenres(tooManyGenres)
            }
        assertEquals("Secondary genres cannot exceed 3 items", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when secondary genres contain duplicates`() {
        // Given
        val duplicatedGenres = listOf(Genre.ADVENTURE, Genre.FANTASY, Genre.ADVENTURE)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                SecondaryGenres(duplicatedGenres)
            }
        assertEquals("Secondary genres cannot contain duplicates", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when secondary genres contain primary genre`() {
        // Given
        val secondaryGenres = SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
        val primaryGenre = Genre.ADVENTURE

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                secondaryGenres.validateNotContainingPrimary(primaryGenre)
            }
        assertEquals("Secondary genres cannot contain the primary genre", exception.message)
    }

    @Test
    fun `should not throw when secondary genres do not contain primary genre`() {
        // Given
        val secondaryGenres = SecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
        val primaryGenre = Genre.FANTASY

        // When & Then - should not throw
        secondaryGenres.validateNotContainingPrimary(primaryGenre)
    }
}
