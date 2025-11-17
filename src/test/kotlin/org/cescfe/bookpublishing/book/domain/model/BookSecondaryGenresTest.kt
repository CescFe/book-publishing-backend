package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BookSecondaryGenresTest {
    @Test
    fun `should create secondary genres with valid data`() {
        // When
        val secondaryGenres = BookSecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))

        // Then
        assertEquals(2, secondaryGenres.value.size)
        assertEquals(Genre.ADVENTURE, secondaryGenres.value[0])
        assertEquals(Genre.HISTORICAL_FICTION, secondaryGenres.value[1])
    }

    @Test
    fun `should create secondary genres with maximum allowed items`() {
        // When
        val secondaryGenres =
            BookSecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION, Genre.MYSTERY))

        // Then
        assertEquals(3, secondaryGenres.value.size)
    }

    @Test
    fun `should throw BookDomainException when secondary genres exceed maximum`() {
        // Given
        val tooManyGenres =
            listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION, Genre.MYSTERY, Genre.ROMANCE)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BookSecondaryGenres(tooManyGenres)
            }
        assertEquals("Secondary genres cannot exceed 3 items", exception.message)
    }

    @Test
    fun `should throw BookDomainException when secondary genres contain duplicates`() {
        // Given
        val duplicatedGenres = listOf(Genre.ADVENTURE, Genre.FANTASY, Genre.ADVENTURE)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                BookSecondaryGenres(duplicatedGenres)
            }
        assertEquals("Secondary genres cannot contain duplicates", exception.message)
    }

    @Test
    fun `should throw BookDomainException when secondary genres contain primary genre`() {
        // Given
        val secondaryGenres = BookSecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
        val primaryGenre = Genre.ADVENTURE

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                secondaryGenres.validateNotContainingPrimary(primaryGenre)
            }
        assertEquals("Secondary genres cannot contain the primary genre", exception.message)
    }

    @Test
    fun `should not throw when secondary genres do not contain primary genre`() {
        // Given
        val secondaryGenres = BookSecondaryGenres(listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION))
        val primaryGenre = Genre.FANTASY

        // When & Then - should not throw
        secondaryGenres.validateNotContainingPrimary(primaryGenre)
    }
}
