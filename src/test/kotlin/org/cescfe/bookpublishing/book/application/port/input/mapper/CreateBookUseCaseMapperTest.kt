package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.book.objectMothers.CreateBookCommandObjectMother
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CreateBookUseCaseMapperTest {
    private val mapper = CreateBookUseCaseMapper()

    @Test
    fun `should map input values to domain book`() {
        // Given
        val input = CreateBookCommandObjectMother.createWithAllFields()

        // When
        val result = mapper.toDomain(input)

        // Then
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
        assertEquals(input.isbn, result.isbn!!.value)
        assertEquals(input.publicationDate, result.publicationDate!!.value)
        assertEquals(input.pageCount, result.pageCount!!.value)
        assertEquals(input.coverImagePath, result.coverImagePath!!.value)
        assertEquals(input.description, result.description!!.value)
        assertEquals(input.status, result.status)
    }

    @Test
    fun `should map input values with minimal fields`() {
        // Given
        val input = CreateBookCommandObjectMother.createMinimal()

        // When
        val result = mapper.toDomain(input)

        // Then
        assertEquals(input.title, result.title.value)
        assertEquals(input.authorId, result.authorId.value)
        assertEquals(input.collectionId, result.collectionId.value)
        assertEquals(input.basePrice, result.basePrice.value)
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
        assertNull(result.status)
    }
}
