package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.springframework.stereotype.Repository

@Repository
class JpaAuthorRepository(
    private val authorJpaEntityRepository: AuthorJpaEntityRepository,
    private val authorMapper: AuthorMapper
) : AuthorRepositoryView {
    override fun findById(id: AuthorId): Author? {
        return authorJpaEntityRepository.findAuthorById(id.value)
            .map { authorMapper.toDomain(it) }
            .orElse(null)
    }

    override fun findAll(): List<Author> {
        return authorJpaEntityRepository.findAllAuthors()
            .map { authorMapper.toDomain(it) }
    }

    override fun save(author: Author): Author {
        val entity = authorMapper.fromDomain(author)
        return authorMapper.toDomain(authorJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: AuthorId) {
        authorJpaEntityRepository.deleteById(id.value)

    }

    override fun existsById(id: AuthorId): Boolean {
        return authorJpaEntityRepository.existsAuthorById(id.value)
    }
}
