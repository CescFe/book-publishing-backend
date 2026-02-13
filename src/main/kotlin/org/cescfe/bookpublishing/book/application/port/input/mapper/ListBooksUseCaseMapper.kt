package org.cescfe.bookpublishing.book.application.port.input.mapper

import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.PaginationMeta
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.springframework.stereotype.Component

@Component
class ListBooksUseCaseMapper {
    fun toPaginatedResult(
        books: List<BookSummary>,
        totalCount: Long,
        page: Int,
        limit: Int,
    ): PaginatedResult<BookSummary> {
        val totalPages = if (totalCount == 0L) 0 else ((totalCount - 1) / limit + 1).toInt()

        return PaginatedResult(
            data = books,
            metadata =
                PaginationMeta(
                    total = totalCount,
                    page = page,
                    limit = limit,
                    totalPages = totalPages,
                ),
        )
    }

    fun toNonPaginatedResult(
        books: List<BookSummary>,
        totalCount: Long,
    ): NonPaginatedResult<BookSummary> {
        return NonPaginatedResult(
            data = books,
            metadata =
                NonPaginationMeta(
                    total = totalCount,
                ),
        )
    }
}
