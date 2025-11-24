package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.book.domain.port.BookRepository
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.mapper.BookPersistenceMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaBookRepository(
    private val bookJpaEntityRepository: BookJpaEntityRepository,
    private val bookMapper: BookPersistenceMapper,
) : BookRepository {
    override fun findById(id: BookId): Book? =
        bookJpaEntityRepository
            .findById(id.value)
            .map { bookMapper.toDomain(it) }
            .orElse(null)

    override fun findAllSummary(): List<BookSummary> =
        bookJpaEntityRepository
            .findAllByOrderByTitleAsc()
            .map { bookMapper.toDomainSummary(it) }

    override fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<BookSummary> {
        val pageable: Pageable = PageRequest.of(page - 1, limit)
        return bookJpaEntityRepository
            .findAllByOrderByTitleAsc(pageable)
            .map { bookMapper.toDomainSummary(it) }
    }

    override fun countAll(): Long = bookJpaEntityRepository.count()

    override fun save(book: Book): Book {
        val entity = bookMapper.fromDomain(book)
        return bookMapper.toDomain(bookJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: BookId) {
        bookJpaEntityRepository.deleteById(id.value)
    }

    override fun existsById(id: BookId): Boolean = bookJpaEntityRepository.existsById(id.value)

    override fun findByTitle(title: String): Book? =
        bookJpaEntityRepository
            .findByTitle(title)
            ?.let { bookMapper.toDomain(it) }
}
