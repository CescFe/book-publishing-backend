package org.cescfe.bookpublishing.author.domain.port

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId

interface AuthorRepositoryView {
    fun findById(id: AuthorId): Author?

    fun findAll(): List<Author>

    fun findAll(
        page: Int,
        limit: Int,
    ): List<Author>

    fun countAll(): Long

    fun save(author: Author): Author

    fun deleteById(id: AuthorId)

    fun removeAuthorRole(id: AuthorId)

    fun existsById(id: AuthorId): Boolean

    fun existsByEmail(email: String): Boolean
}
