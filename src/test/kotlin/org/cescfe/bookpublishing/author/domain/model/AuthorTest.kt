package org.cescfe.bookpublishing.author.domain.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AuthorTest {
    @Test
    fun `should create author with valid data`() {
        // Given
        val id = AuthorId.generate()
        val fullName = FullName("John Ronald Reuel Tolkien")
        val pseudonym = Pseudonym("J.R.R. Tolkien")
        val email = Email("tolkien@example.com")
        val website = Website("https://www.tolkiensociety.org")
        val biography = Biography("John Ronald Reuel Tolkien was an English writer...")

        // When
        val author =
            Author(
                id = id,
                fullName = fullName,
                pseudonym = pseudonym,
                email = email,
                website = website,
                biography = biography,
            )

        // Then
        assertEquals(id, author.id)
        assertEquals(fullName, author.fullName)
        assertEquals(pseudonym, author.pseudonym)
        assertEquals(email, author.email)
        assertEquals(website, author.website)
        assertEquals(biography, author.biography)
    }
}
