package org.cescfe.bookpublishing.book.application.port.input

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.book.domain.model.BookSummary

interface ListBooksUseCase {
    fun execute(query: Query): PaginatedResult<BookSummary>

    data class Query(
        val page: Int,
        val limit: Int,
    )
}
