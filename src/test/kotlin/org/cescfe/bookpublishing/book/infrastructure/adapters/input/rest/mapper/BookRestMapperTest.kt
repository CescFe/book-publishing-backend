package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.book.objectMothers.BookObjectMother
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BookRestMapperTest {
    private val mapper = BookRestMapper()

    @Test
    fun `should map domain book to response DTO`() {
        // Given
        val audit =
            Metadata(
                createdAt = LocalDateTime.now(),
                createdBy = "admin",
                updatedAt = LocalDateTime.now(),
                updatedBy = "admin",
            )
        val book = BookObjectMother.createWithAllFields().copy(audit = audit)

        // When
        val dto = mapper.toDto(book)

        // Then
        assertEquals(book.id.value, dto.id)
        assertEquals(book.title.value, dto.title)
        assertEquals(book.authorId.value, dto.author!!.id)
        assertEquals(book.authorName, dto.author!!.name)
        assertEquals(book.collectionId.value, dto.collection!!.id)
        assertEquals(book.collectionName, dto.collection!!.name)
        assertEquals(book.readingLevel!!.name, dto.readingLevel!!.name)
        assertEquals(book.primaryLanguage!!.name, dto.primaryLanguage!!.name)
        assertEquals(book.secondaryLanguages!!.value.map { it.name }, dto.secondaryLanguages!!.map { it.name })
        assertEquals(book.primaryGenre!!.name, dto.primaryGenre!!.name)
        assertEquals(book.secondaryGenres!!.value.map { it.name }, dto.secondaryGenres!!.map { it.name })
        assertEquals(book.basePrice.value, dto.basePrice)
        assertEquals(book.vatRate!!.value, dto.vatRate!!)
        assertEquals(book.finalPrice, dto.finalPrice)
        assertEquals(book.isbn!!.value, dto.isbn)
        assertEquals(book.publicationDate!!.value, dto.publicationDate)
        assertEquals(book.pageCount!!.value, dto.pageCount)
        assertEquals(book.coverImagePath!!.value, dto.coverImagePath)
        assertEquals(book.description!!.value, dto.description)
        assertEquals(book.status!!.name, dto.status!!.name)
        assertEquals(book.audit!!.createdAt!!.atOffset(java.time.ZoneOffset.UTC), dto.createdAt)
        assertEquals(book.audit.createdBy, dto.createdBy)
        assertEquals(book.audit.updatedAt!!.atOffset(java.time.ZoneOffset.UTC), dto.updatedAt)
        assertEquals(book.audit.updatedBy, dto.updatedBy)
    }

    @Test
    fun `should map minimal domain book to response DTO`() {
        // Given
        val book = BookObjectMother.createMinimal()

        // When
        val dto = mapper.toDto(book)

        // Then
        assertNotNull(dto)
        assertEquals(book.id.value, dto.id)
        assertEquals(book.authorId.value, dto.author!!.id)
        assertEquals(book.authorName, dto.author!!.name)
        assertEquals(book.collectionId.value, dto.collection!!.id)
        assertEquals(book.collectionName, dto.collection!!.name)
        assertEquals(book.title.value, dto.title)
        assertEquals(book.basePrice.value, dto.basePrice)
        assertEquals(book.finalPrice, dto.finalPrice)
        assertNull(dto.readingLevel)
        assertNull(dto.primaryLanguage)
        assertNull(dto.secondaryLanguages)
        assertNull(dto.primaryGenre)
        assertNull(dto.secondaryGenres)
        assertNull(dto.vatRate)
        assertNull(dto.isbn)
        assertNull(dto.publicationDate)
        assertNull(dto.pageCount)
        assertNull(dto.coverImagePath)
        assertNull(dto.description)
        assertNull(dto.status)
    }
}
