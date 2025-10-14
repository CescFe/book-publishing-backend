package org.cescfe.bookpublishing.author.domain.port

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.SearchCriteria

interface AuthorRepositoryView {
    fun findById(id: AuthorId): Author?

    fun findAll(): List<Author>

    fun findAll(searchCriteria: SearchCriteria): List<Author>

    fun count(searchCriteria: SearchCriteria): Long

    fun save(author: Author): Author

    fun deleteById(id: AuthorId)

    fun existsById(id: AuthorId): Boolean

    fun findByEmail(email: String): Author?
}
