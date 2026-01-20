package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.book.objectMothers.UpdateBookCommandObjectMother
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UpdateBookUseCaseMapperTest {
    private val mapper = UpdateBookUseCaseMapper()

    @Test
    fun `should map input values to domain book`() {
        // Given
        val existingBook = BookObjectMother.createWithAllFields()
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Updated Book Title",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
                readingLevel = ReadingLevel.YOUNG_ADULT,
                primaryLanguage = Language.SPANISH,
                secondaryLanguages = listOf(Language.ENGLISH),
                primaryGenre = Genre.SCIENCE_FICTION,
                secondaryGenres = listOf(Genre.THRILLER),
                basePrice = 24.99,
                vatRate = 0.10,
                isbn = "9781234567890",
                publicationDate = java.time.LocalDate.of(2024, 1, 15),
                pageCount = 500,
                coverImagePath = "/images/updated-cover.jpg",
                description = "Updated description",
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(existingBook.id, result.id)
        assertEquals(existingBook.id, result.id)
        assertEquals(input.title, result.title.value)
        assertEquals(input.authorId, result.authorId.value)
        assertEquals(input.collectionId, result.collectionId.value)
        assertEquals(input.readingLevel, result.readingLevel)
        assertEquals(input.primaryLanguage, result.primaryLanguage)
        assertEquals(input.secondaryLanguages, result.secondaryLanguages!!.value)
        assertEquals(input.primaryGenre, result.primaryGenre)
        assertEquals(input.secondaryGenres, result.secondaryGenres!!.value)
        assertEquals(input.basePrice, result.basePrice.value)
        assertEquals(input.vatRate, result.vatRate!!.value)
        assertEquals(27.49, result.finalPrice)
        assertEquals(input.isbn, result.isbn!!.value)
        assertEquals(input.publicationDate, result.publicationDate!!.value)
        assertEquals(input.pageCount, result.pageCount!!.value)
        assertEquals(input.coverImagePath, result.coverImagePath!!.value)
        assertEquals(input.description, result.description!!.value)
        assertEquals(Status.DRAFT, result.status)
        assertEquals(existingBook.audit, result.audit)
    }

    @Test
    fun `should map input values with minimal fields`() {
        // Given
        val existingBook = BookObjectMother.createMinimal()
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Updated Minimal Book",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
                basePrice = 15.99,
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(existingBook.id, result.id)
        assertEquals(input.title, result.title.value)
        assertEquals(input.authorId, result.authorId.value)
        assertEquals(input.collectionId, result.collectionId.value)
        assertEquals(input.basePrice, result.basePrice.value)
        assertEquals(16.63, result.finalPrice)
        assertEquals(Status.DRAFT, result.status)
        assertEquals(existingBook.audit, result.audit)
        assertNull(result.readingLevel)
        assertNull(result.primaryLanguage)
        assertNull(result.secondaryLanguages)
        assertNull(result.primaryGenre)
        assertNull(result.secondaryGenres)
        assertNull(result.vatRate)
        assertNull(result.isbn)
        assertNull(result.publicationDate)
        assertNull(result.pageCount)
        assertNull(result.coverImagePath)
        assertNull(result.description)
    }

    @Test
    fun `should preserve existing book id and audit`() {
        // Given
        val existingBookId = UUID.randomUUID()
        val existingAudit =
            Metadata(
                createdAt = LocalDateTime.of(2023, 1, 1, 10, 0),
                createdBy = "test-user",
                updatedAt = LocalDateTime.of(2023, 1, 2, 10, 0),
                updatedBy = "test-user-updated",
            )
        val existingBook =
            BookObjectMother.create(
                id = existingBookId,
                audit = existingAudit,
            )
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Updated Title",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(existingBookId, result.id.value)
        assertEquals(existingAudit, result.audit)
    }

    @Test
    fun `should calculate final price correctly with custom vat rate`() {
        // Given
        val existingBook = BookObjectMother.createMinimal()
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Test Book",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
                basePrice = 20.00,
                vatRate = 0.21,
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(24.20, result.finalPrice)
    }

    @Test
    fun `should calculate final price correctly with default vat rate`() {
        // Given
        val existingBook = BookObjectMother.createMinimal()
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Test Book",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
                basePrice = 10.00,
                vatRate = null,
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(10.40, result.finalPrice)
    }

    @Test
    fun `should use default status when status is null`() {
        // Given
        val existingBook = BookObjectMother.createMinimal()
        val input =
            UpdateBookCommandObjectMother.create(
                title = "Test Book",
                authorId = existingBook.authorId.value,
                collectionId = existingBook.collectionId.value,
            )

        // When
        val result = mapper.toDomain(input, existingBook)

        // Then
        assertEquals(Status.DRAFT, result.status)
    }
}
