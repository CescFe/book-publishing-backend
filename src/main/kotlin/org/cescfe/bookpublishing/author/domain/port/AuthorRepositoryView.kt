package org.cescfe.bookpublishing.author.domain.port

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId

interface AuthorRepositoryView {
    fun findById(id: AuthorId): Author?

    fun findAll(): List<Author>

    fun save(author: Author): Author

    fun deleteById(id: AuthorId)

    fun existsById(id: AuthorId): Boolean
}
