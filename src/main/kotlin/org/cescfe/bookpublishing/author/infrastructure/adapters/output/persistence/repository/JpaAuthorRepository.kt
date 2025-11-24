package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.port.AuthorRepository
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper.AuthorPersistenceMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaAuthorRepository(
    private val authorJpaEntityRepository: AuthorJpaEntityRepository,
    private val authorMapper: AuthorPersistenceMapper,
) : AuthorRepository {
    override fun findById(id: AuthorId): Author? =
        authorJpaEntityRepository
            .findById(id.value)
            .map { authorMapper.toDomain(it) }
            .orElse(null)

    override fun findAllSummary(): List<AuthorSummary> =
        authorJpaEntityRepository
            .findAllByOrderByFullNameAsc()
            .map { authorMapper.toDomainSummary(it) }

    override fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<AuthorSummary> {
        val pageable: Pageable = PageRequest.of(page - 1, limit)
        return authorJpaEntityRepository
            .findAllByOrderByFullNameAsc(pageable)
            .map { authorMapper.toDomainSummary(it) }
    }

    override fun countAll(): Long = authorJpaEntityRepository.count()

    override fun save(author: Author): Author {
        val entity = authorMapper.fromDomain(author)
        return authorMapper.toDomain(authorJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: AuthorId) {
        authorJpaEntityRepository.deleteById(id.value)
    }

    override fun existsById(id: AuthorId): Boolean = authorJpaEntityRepository.existsById(id.value)

    override fun findByEmail(email: String): Author? =
        authorJpaEntityRepository
            .findByEmail(email)
            ?.let { authorMapper.toDomain(it) }

    override fun existsByEmail(email: String): Boolean = authorJpaEntityRepository.existsByEmail(email)
}
