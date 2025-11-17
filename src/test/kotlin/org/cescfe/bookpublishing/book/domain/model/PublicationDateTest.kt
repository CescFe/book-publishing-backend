package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals

class PublicationDateTest {
    @Test
    fun `should create publication date with valid date`() {
        // When
        val publicationDate = PublicationDate(LocalDate.of(2020, 10, 6))

        // Then
        assertEquals(LocalDate.of(2020, 10, 6), publicationDate.value)
    }

    @Test
    fun `should create publication date from string`() {
        // When
        val publicationDate = PublicationDate.fromString("2020-10-06")

        // Then
        assertEquals(LocalDate.of(2020, 10, 6), publicationDate.value)
    }

    @Test
    fun `should throw BookDomainException when publication date string is invalid`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                PublicationDate.fromString("invalid-date")
            }
        assertEquals("Publication date must be a valid date", exception.message)
    }

    @Test
    fun `should throw BookDomainException when publication date string has wrong format`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                PublicationDate.fromString("10-06-2020")
            }
        assertEquals("Publication date must be a valid date", exception.message)
    }
}
