package org.cescfe.bookpublishing.author.domain.port

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthorRepositoryViewTest {
    @Test
    fun `should implement repository contract correctly`() {
        // Given
        val repository = createTestRepository()
        val author =
            Author(
                id = AuthorId.generate(),
                fullName = FullName("John Tolkien"),
                roles = setOf(AuthorRole.AUTHOR),
            )

        // When & Then
        val savedAuthor = repository.save(author)
        assertEquals(author.id, savedAuthor.id)

        val foundAuthor = repository.findById(author.id)
        assertEquals(author.id, foundAuthor?.id)

        assertTrue(repository.existsById(author.id))

        repository.deleteById(author.id)
        assertFalse(repository.existsById(author.id))
        assertNull(repository.findById(author.id))
    }

    private fun createTestRepository(): AuthorRepositoryView {
        return object : AuthorRepositoryView {
            private val authors = mutableMapOf<AuthorId, Author>()

            override fun findById(id: AuthorId): Author? = authors[id]

            override fun findAll(): List<Author> = authors.values.toList()

            override fun save(author: Author): Author {
                authors[author.id] = author
                return author
            }

            override fun deleteById(id: AuthorId) {
                authors.remove(id)
            }

            override fun existsById(id: AuthorId): Boolean = authors.containsKey(id)
        }
    }
}
