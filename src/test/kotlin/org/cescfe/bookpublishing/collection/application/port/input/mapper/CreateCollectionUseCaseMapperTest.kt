package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.cescfe.bookpublishing.collection.objectMothers.CreateCollectionCommandObjectMother
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun `should throw CollectionDomainException when name is blank`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                name = "",
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Collection name cannot be blank", exception.message)
        assertEquals("NAME_CANNOT_BE_BLANK", exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when name is too long`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                name = "a".repeat(81),
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Collection name must be between 1 and 80 characters", exception.message)
        assertEquals("NAME_TOO_LONG", exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when secondary languages exceed 3 items`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                secondaryLanguages = listOf(
                    Language.CATALAN,
                    Language.SPANISH,
                    Language.ENGLISH,
                    Language.VALENCIAN,
                ),
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Secondary languages cannot exceed 3 items", exception.message)
        assertEquals("SECONDARY_LANGUAGES_TOO_MANY", exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when secondary languages contain duplicates`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                secondaryLanguages = listOf(
                    Language.CATALAN,
                    Language.SPANISH,
                    Language.CATALAN,
                ),
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Secondary languages cannot contain duplicates", exception.message)
        assertEquals("SECONDARY_LANGUAGES_DUPLICATED", exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when secondary genres exceed 3 items`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                secondaryGenres = listOf(
                    Genre.ADVENTURE,
                    Genre.FANTASY,
                    Genre.HISTORICAL_FICTION,
                    Genre.SCIENCE_FICTION,
                ),
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Secondary genres cannot exceed 3 items", exception.message)
        assertEquals("SECONDARY_GENRES_TOO_MANY", exception.subType)
    }

    @Test
    fun `should throw CollectionDomainException when secondary genres contain duplicates`() {
        // Given
        val input =
            CreateCollectionCommandObjectMother.create(
                secondaryGenres = listOf(
                    Genre.ADVENTURE,
                    Genre.FANTASY,
                    Genre.ADVENTURE,
                ),
            )

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                mapper.toDomain(input)
            }
        assertEquals("Secondary genres cannot contain duplicates", exception.message)
        assertEquals("SECONDARY_GENRES_DUPLICATED", exception.subType)
    }
}
