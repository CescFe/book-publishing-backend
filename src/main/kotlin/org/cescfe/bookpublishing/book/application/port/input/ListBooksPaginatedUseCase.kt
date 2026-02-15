package org.cescfe.bookpublishing.book.application.port.input

import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult

interface ListBooksPaginatedUseCase {
    fun execute(query: Query): PaginatedResult<BookSummary>

    data class Query(
        val page: Int,
        val limit: Int,
    )
}
