package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.entity.BookEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookJpaEntityRepository : JpaRepository<BookEntity, UUID> {
    fun findByTitle(title: String): BookEntity?

    fun findByIsbn(isbn: String): BookEntity?

    fun existsByIsbn(isbn: String): Boolean

    fun findAllByOrderByTitleAsc(): List<BookEntity>

    fun findAllByOrderByTitleAsc(pageable: Pageable): List<BookEntity>
}
