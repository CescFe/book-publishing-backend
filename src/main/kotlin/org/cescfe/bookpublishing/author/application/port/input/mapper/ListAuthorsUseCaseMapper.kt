package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.PaginationMeta
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.springframework.stereotype.Component

@Component
class ListAuthorsUseCaseMapper {
    fun toPaginatedResult(
        authors: List<AuthorSummary>,
        totalCount: Long,
        page: Int,
        limit: Int,
    ): PaginatedResult<AuthorSummary> {
        val totalPages = if (totalCount == 0L) 0 else ((totalCount - 1) / limit + 1).toInt()

        return PaginatedResult(
            data = authors,
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
        authors: List<AuthorSummary>,
        totalCount: Long,
    ): NonPaginatedResult<AuthorSummary> {
        return NonPaginatedResult(
            data = authors,
            metadata =
                NonPaginationMeta(
                    total = totalCount,
                ),
        )
    }
}
