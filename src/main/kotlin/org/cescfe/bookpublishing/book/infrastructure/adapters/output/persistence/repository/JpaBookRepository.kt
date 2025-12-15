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
            .findByIdWithRelations(id.value)
            ?.let { bookMapper.toDomainWithRelations(it) }

    override fun findAllSummary(): List<BookSummary> =
        bookJpaEntityRepository
            .findAllProjectedByOrderByTitleAsc()
            .map { bookMapper.toDomainSummaryWithRelations(it) }

    override fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<BookSummary> {
        val pageable: Pageable = PageRequest.of(page - 1, limit)
        return bookJpaEntityRepository
            .findAllProjectedByOrderByTitleAsc(pageable)
            .map { bookMapper.toDomainSummaryWithRelations(it) }
    }

    override fun countAll(): Long = bookJpaEntityRepository.count()

    override fun save(book: Book): Book {
        val entity = bookMapper.fromDomain(book)
        val saved = bookJpaEntityRepository.save(entity)
        return findById(BookId(saved.id))!!
    }

    override fun deleteById(id: BookId) {
        bookJpaEntityRepository.deleteById(id.value)
    }

    override fun existsById(id: BookId): Boolean = bookJpaEntityRepository.existsById(id.value)

    override fun findByTitle(title: String): Book? =
        bookJpaEntityRepository
            .findByTitle(title)
            ?.let { bookMapper.toDomain(it) }

    override fun findByIsbn(isbn: String): Book? =
        bookJpaEntityRepository
            .findByIsbn(isbn)
            ?.let { bookMapper.toDomain(it) }

    override fun existsByIsbn(isbn: String): Boolean = bookJpaEntityRepository.existsByIsbn(isbn)
}
