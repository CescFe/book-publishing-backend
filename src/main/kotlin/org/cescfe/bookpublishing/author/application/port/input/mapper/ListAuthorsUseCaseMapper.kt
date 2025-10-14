package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.author.domain.model.SearchCriteria
import org.springframework.stereotype.Component

@Component
class ListAuthorsUseCaseMapper {
    fun toSearchCriteria(input: ListAuthorsUseCase.InputValues): SearchCriteria =
        SearchCriteria(
            searchTerm = input.search,
            page = input.page,
            limit = input.limit,
        )

    fun toPaginatedResult(
        authors: List<Author>,
        totalCount: Long,
        page: Int,
        limit: Int,
    ): PaginatedResult<Author> {
        val totalPages = if (totalCount == 0L) 0 else ((totalCount - 1) / limit + 1).toInt()

        return PaginatedResult(
            data = authors,
            meta =
                PaginationMeta(
                    total = totalCount,
                    page = page,
                    limit = limit,
                    totalPages = totalPages,
                ),
        )
    }
}
