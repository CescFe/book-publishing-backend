package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult

interface ListCollectionsPaginatedUseCase {
    fun execute(query: Query): PaginatedResult<CollectionSummary>

    data class Query(
        val page: Int,
        val limit: Int,
    )
}
