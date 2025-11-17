package org.cescfe.bookpublishing.author.domain.model

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class WebsiteTest {
    @Test
    fun `should create website with valid URL`() {
        // When
        val website = Website("https://www.tolkiensociety.org")

        // Then
        assertEquals("https://www.tolkiensociety.org", website.value)
    }

    @Test
    fun `should throw AuthorValidationException when website is blank`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Website("   ")
        }
    }

    @Test
    fun `should throw AuthorValidationException when website does not start with http`() {
        // When & Then
        assertThrows<AuthorDomainException> {
            Website("www.example.com")
        }
    }
}
