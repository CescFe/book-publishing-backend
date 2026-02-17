package org.cescfe.bookpublishing.author.domain.port

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary

interface AuthorRepository {
    fun findById(id: AuthorId): Author?

    fun findAllSummary(): List<AuthorSummary>

    fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<AuthorSummary>

    fun countAll(): Long

    fun save(author: Author): Author

    fun deleteById(id: AuthorId)

    fun existsById(id: AuthorId): Boolean

    fun findByEmail(email: String): Author?

    fun existsByEmail(email: String): Boolean
}
