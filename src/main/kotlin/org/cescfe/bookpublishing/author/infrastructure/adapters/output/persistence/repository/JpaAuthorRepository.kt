package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper.AuthorPersistenceMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaAuthorRepository(
    private val authorJpaEntityRepository: AuthorJpaEntityRepository,
    private val authorMapper: AuthorPersistenceMapper,
) : AuthorRepositoryView {
    override fun findById(id: AuthorId): Author? =
        authorJpaEntityRepository
            .findAuthorById(id.value)
            .map { authorMapper.toDomain(it) }
            .orElse(null)

    override fun findAllSummary(): List<AuthorSummary> =
        authorJpaEntityRepository
            .findAllAuthors()
            .map { authorMapper.toDomainSummary(it) }

    override fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<AuthorSummary> {
        val pageable: Pageable = PageRequest.of(page - 1, limit)
        return authorJpaEntityRepository
            .findAllAuthors(pageable)
            .map { authorMapper.toDomainSummary(it) }
    }

    override fun countAll(): Long = authorJpaEntityRepository.countAllAuthors()

    override fun save(author: Author): Author {
        val entity = authorMapper.fromDomain(author)
        return authorMapper.toDomain(authorJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: AuthorId) {
        authorJpaEntityRepository.deleteById(id.value)
    }

    override fun removeAuthorRole(id: AuthorId) {
        authorJpaEntityRepository.removeAuthorRole(id.value)
    }

    override fun existsById(id: AuthorId): Boolean = authorJpaEntityRepository.existsAuthorById(id.value)

    override fun findByEmail(email: String): Author? =
        authorJpaEntityRepository
            .findByEmail(email)
            .map { authorMapper.toDomain(it) }
            .orElse(null)

    override fun existsByEmail(email: String): Boolean = authorJpaEntityRepository.existsByEmail(email)
}
