package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.springframework.stereotype.Component

@Component
class ListCollectionsUseCaseMapper {
    fun toPaginatedResult(
        collections: List<CollectionSummary>,
        totalCount: Long,
        page: Int,
        limit: Int,
    ): PaginatedResult<CollectionSummary> {
        val totalPages = if (totalCount == 0L) 0 else ((totalCount - 1) / limit + 1).toInt()

        return PaginatedResult(
            data = collections,
            metadata =
                PaginationMeta(
                    total = totalCount,
                    page = page,
                    limit = limit,
                    totalPages = totalPages,
                ),
        )
    }
}
