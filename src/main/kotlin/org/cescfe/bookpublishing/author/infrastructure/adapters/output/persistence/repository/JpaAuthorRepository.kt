package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.SearchCriteria
import org.cescfe.bookpublishing.author.domain.port.AuthorRepositoryView
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper.AuthorMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaAuthorRepository(
    private val authorJpaEntityRepository: AuthorJpaEntityRepository,
    private val authorMapper: AuthorMapper,
) : AuthorRepositoryView {
    override fun findById(id: AuthorId): Author? =
        authorJpaEntityRepository
            .findAuthorById(id.value)
            .map { authorMapper.toDomain(it) }
            .orElse(null)

    override fun findAll(): List<Author> =
        authorJpaEntityRepository
            .findAllAuthors()
            .map { authorMapper.toDomain(it) }

    override fun findAll(searchCriteria: SearchCriteria): List<Author> {
        val pageable: Pageable = PageRequest.of(searchCriteria.page - 1, searchCriteria.limit)

        return if (searchCriteria.searchTerm.isNullOrBlank()) {
            authorJpaEntityRepository
                .findAllAuthors()
                .map { authorMapper.toDomain(it) }
        } else {
            authorJpaEntityRepository
                .findAllAuthorsWithSearch(searchCriteria.searchTerm, pageable)
                .map { authorMapper.toDomain(it) }
        }
    }

    override fun count(searchCriteria: SearchCriteria): Long =
        authorJpaEntityRepository.countAuthorsWithSearch(searchCriteria.searchTerm)

    override fun save(author: Author): Author {
        val entity = authorMapper.fromDomain(author)
        return authorMapper.toDomain(authorJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: AuthorId) {
        authorJpaEntityRepository.deleteById(id.value)
    }

    override fun existsById(id: AuthorId): Boolean = authorJpaEntityRepository.existsAuthorById(id.value)

    override fun findByEmail(email: String): Author? =
        authorJpaEntityRepository
            .findByEmail(email)
            .map { authorMapper.toDomain(it) }
            .orElse(null)
}
