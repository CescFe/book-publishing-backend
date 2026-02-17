package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.PaginationMeta
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

    fun toNonPaginatedResult(
        collections: List<CollectionSummary>,
        totalCount: Long,
    ): NonPaginatedResult<CollectionSummary> =
        NonPaginatedResult(
            data = collections,
            metadata =
                NonPaginationMeta(
                    total = totalCount,
                ),
        )
}
