package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.book.objectMothers.BookEntityObjectMother
import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookPersistenceMapperTest {
    private val bookMapper = BookPersistenceMapper()

    @Test
    fun `should map book domain to entity correctly`() {
        // Given
        val book = BookObjectMother.createWithAllFields()

        // When
        val result = bookMapper.fromDomain(book)

        // Then
        assertEquals(book.id.value, result.id)
        assertEquals(book.title.value, result.title)
        assertEquals(book.authorId.value, result.authorId)
        assertEquals(book.collectionId.value, result.collectionId)
        assertEquals(book.readingLevel, result.readingLevel)
        assertEquals(book.primaryLanguage, result.primaryLanguage)
        assertEquals(
            book.secondaryLanguages!!.value,
            result.secondaryLanguages,
        )
        assertEquals(book.primaryGenre, result.primaryGenre)
        assertEquals(
            book.secondaryGenres!!.value,
            result.secondaryGenres,
        )
        assertEquals(book.basePrice.value, result.basePrice)
        assertEquals(book.vatRate!!.value, result.vatRate)
        assertEquals(book.calculateFinalPrice(), result.finalPrice)
        assertEquals(book.isbn!!.value, result.isbn)
        assertEquals(book.publicationDate!!.value, result.publicationDate)
        assertEquals(book.pageCount!!.value, result.pageCount)
        assertEquals(book.coverImagePath!!.value, result.coverImagePath)
        assertEquals(book.description!!.value, result.description)
        assertEquals(book.status, result.status)
    }

    @Test
    fun `should map book with minimal data correctly`() {
        // Given
        val book = BookObjectMother.createMinimal()

        // When
        val result = bookMapper.fromDomain(book)

        // Then
        assertEquals(book.id.value, result.id)
        assertEquals(book.title.value, result.title)
        assertEquals(book.authorId.value, result.authorId)
        assertEquals(book.collectionId.value, result.collectionId)
        assertEquals(book.basePrice.value, result.basePrice)
        assertEquals(book.calculateFinalPrice(), result.finalPrice)
        assertEquals(book.status, result.status)
    }

    @Test
    fun `should map entity to book domain correctly`() {
        // Given
        val entity = BookEntityObjectMother.createWithAllFields()

        // When
        val result = bookMapper.toDomain(entity)

        // Then
        assertEquals(entity.id, result.id.value)
        assertEquals(entity.title, result.title.value)
        assertEquals(entity.authorId, result.authorId.value)
        assertEquals(entity.collectionId, result.collectionId.value)
        assertEquals(entity.readingLevel, result.readingLevel)
        assertEquals(entity.primaryLanguage, result.primaryLanguage)
        assertEquals(
            entity.secondaryLanguages,
            result.secondaryLanguages!!.value,
        )
        assertEquals(entity.primaryGenre, result.primaryGenre)
        assertEquals(
            entity.secondaryGenres,
            result.secondaryGenres!!.value,
        )
        assertEquals(entity.basePrice, result.basePrice.value)
        assertEquals(entity.vatRate, result.vatRate!!.value)
        assertEquals(entity.isbn, result.isbn!!.value)
        assertEquals(entity.publicationDate, result.publicationDate!!.value)
        assertEquals(entity.pageCount, result.pageCount!!.value)
        assertEquals(entity.coverImagePath, result.coverImagePath!!.value)
        assertEquals(entity.description, result.description!!.value)
        assertEquals(entity.status, result.status)
    }

    @Test
    fun `should map entity with minimal data to book domain correctly`() {
        // Given
        val entity = BookEntityObjectMother.createMinimal()

        // When
        val result = bookMapper.toDomain(entity)

        // Then
        assertEquals(entity.id, result.id.value)
        assertEquals(entity.title, result.title.value)
        assertEquals(entity.authorId, result.authorId.value)
        assertEquals(entity.collectionId, result.collectionId.value)
        assertEquals(entity.basePrice, result.basePrice.value)
        assertEquals(entity.status, result.status)
    }
}
