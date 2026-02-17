package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.collection.objectMothers.UpdateCollectionCommandObjectMother
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateCollectionUseCaseMapperTest {
    private val mapper = UpdateCollectionUseCaseMapper()

    @Test
    fun `should map input values to domain collection`() {
        // Given
        val existingCollection = CollectionObjectMother.createWithAllFields()
        val input =
            UpdateCollectionCommandObjectMother.create(
                name = "Updated Collection Name",
                readingLevel = ReadingLevel.YOUNG_ADULT,
                primaryLanguage = Language.ENGLISH,
                secondaryLanguages = listOf(Language.SPANISH),
                primaryGenre = Genre.FANTASY,
                secondaryGenres = listOf(Genre.ADVENTURE, Genre.GRAMMAR),
            )

        // When
        val result = mapper.toDomain(input, existingCollection)

        // Then
        assertEquals(existingCollection.id, result.id)
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
        val existingCollection = CollectionObjectMother.createMinimal()
        val input =
            UpdateCollectionCommandObjectMother.create(
                name = "Updated Minimal Collection",
            )

        // When
        val result = mapper.toDomain(input, existingCollection)

        // Then
        assertEquals(existingCollection.id, result.id)
        assertEquals(input.name, result.name.value)
        assertEquals(existingCollection.readingLevel, result.readingLevel)
        assertEquals(existingCollection.primaryLanguage, result.primaryLanguage)
        assertEquals(existingCollection.secondaryLanguages, result.secondaryLanguages)
        assertEquals(existingCollection.primaryGenre, result.primaryGenre)
        assertEquals(existingCollection.secondaryGenres, result.secondaryGenres)
    }

    @Test
    fun `should preserve existing collection id and audit`() {
        // Given
        val existingCollectionId = UUID.randomUUID()
        val existingAudit =
            Metadata(
                createdAt = LocalDateTime.of(2023, 1, 1, 10, 0),
                createdBy = "test-user",
                updatedAt = LocalDateTime.of(2023, 1, 2, 10, 0),
                updatedBy = "test-user-updated",
            )
        val existingCollection =
            CollectionObjectMother.create(
                id = existingCollectionId,
                audit = existingAudit,
            )
        val input =
            UpdateCollectionCommandObjectMother.create(
                name = "Updated Collection Name",
            )

        // When
        val result = mapper.toDomain(input, existingCollection)

        // Then
        assertEquals(existingCollectionId, result.id.value)
        assertEquals(existingAudit, result.audit)
    }
}
