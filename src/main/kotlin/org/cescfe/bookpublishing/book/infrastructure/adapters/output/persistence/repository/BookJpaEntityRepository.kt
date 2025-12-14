package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity.BookEntity
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection.BookSummaryProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookJpaEntityRepository : JpaRepository<BookEntity, UUID> {
    fun findByTitle(title: String): BookEntity?

    fun findByIsbn(isbn: String): BookEntity?

    fun existsByIsbn(isbn: String): Boolean

    @Query(
        """
        SELECT b.id as id, b.title as title,
               b.author_id as authorId, a.full_name as authorName,
               b.collection_id as collectionId, c.name as collectionName,
               b.base_price as basePrice, b.final_price as finalPrice,
               b.isbn as isbn, b.status as status
        FROM publishing.book b
        JOIN publishing.author a ON b.author_id = a.id
        JOIN publishing.collection c ON b.collection_id = c.id
        ORDER BY b.title
        """,
        nativeQuery = true
    )
    fun findAllProjectedByOrderByTitleAsc(): List<BookSummaryProjection>

    @Query(
        """
        SELECT b.id as id, b.title as title,
               b.author_id as authorId, a.full_name as authorName,
               b.collection_id as collectionId, c.name as collectionName,
               b.base_price as basePrice, b.final_price as finalPrice,
               b.isbn as isbn, b.status as status
        FROM publishing.book b
        JOIN publishing.author a ON b.author_id = a.id
        JOIN publishing.collection c ON b.collection_id = c.id
        ORDER BY b.title
        """,
        countQuery = """
            SELECT COUNT(*) FROM publishing.book b
            JOIN publishing.author a ON b.author_id = a.id
            JOIN publishing.collection c ON b.collection_id = c.id
        """,
        nativeQuery = true
    )
    fun findAllProjectedByOrderByTitleAsc(pageable: Pageable): List<BookSummaryProjection>
}
