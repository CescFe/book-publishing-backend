package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class AuthorIdsTest {
    @Test
    fun `should create author ids with valid data`() {
        // Given
        val authorId1 = AuthorIdRef(UUID.randomUUID())
        val authorId2 = AuthorIdRef(UUID.randomUUID())

        // When
        val authorIds = AuthorIds(listOf(authorId1, authorId2))

        // Then
        assertEquals(2, authorIds.value.size)
        assertEquals(authorId1, authorIds.value[0])
        assertEquals(authorId2, authorIds.value[1])
    }

    @Test
    fun `should create author ids with single author`() {
        // Given
        val authorId = AuthorIdRef(UUID.randomUUID())

        // When
        val authorIds = AuthorIds(listOf(authorId))

        // Then
        assertEquals(1, authorIds.value.size)
        assertEquals(authorId, authorIds.value[0])
    }

    @Test
    fun `should throw BookDomainException when author ids is empty`() {
        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                AuthorIds(emptyList())
            }
        assertEquals("Book must have at least one author", exception.message)
    }

    @Test
    fun `should throw BookDomainException when author ids contain duplicates`() {
        // Given
        val authorId = AuthorIdRef(UUID.randomUUID())
        val duplicatedAuthorIds = listOf(authorId, authorId)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                AuthorIds(duplicatedAuthorIds)
            }
        assertEquals("Author IDs cannot contain duplicates", exception.message)
    }
}
