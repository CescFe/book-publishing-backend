package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BookSecondaryLanguagesTest {
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
    fun `should throw BookDomainException when secondary languages exceed maximum`() {
        // Given
        val tooManyLanguages =
            listOf(Language.CATALAN, Language.SPANISH, Language.ENGLISH, Language.VALENCIAN)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                SecondaryLanguages(tooManyLanguages)
            }
        assertEquals("Secondary languages cannot exceed 3 items", exception.message)
    }

    @Test
    fun `should throw BookDomainException when secondary languages contain duplicates`() {
        // Given
        val duplicatedLanguages = listOf(Language.CATALAN, Language.SPANISH, Language.CATALAN)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                SecondaryLanguages(duplicatedLanguages)
            }
        assertEquals("Secondary languages cannot contain duplicates", exception.message)
    }

    @Test
    fun `should throw BookDomainException when secondary languages contain primary language`() {
        // Given
        val secondaryLanguages = SecondaryLanguages(listOf(Language.CATALAN, Language.SPANISH))
        val primaryLanguage = Language.CATALAN

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
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
