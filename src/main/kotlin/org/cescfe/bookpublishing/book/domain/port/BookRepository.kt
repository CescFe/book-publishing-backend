package org.cescfe.bookpublishing.book.domain.port

import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.BookId
import org.cescfe.bookpublishing.book.domain.model.BookSummary

interface BookRepository {
    fun findById(id: BookId): Book?

    fun findAllSummary(): List<BookSummary>

    fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<BookSummary>

    fun countAll(): Long

    fun save(book: Book): Book

    fun deleteById(id: BookId)

    fun existsById(id: BookId): Boolean

    fun findByTitle(title: String): Book?
}
