package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary

interface ListCollectionsUseCase {
    fun execute(query: Query): PaginatedResult<CollectionSummary>

    data class Query(
        val page: Int,
        val limit: Int,
    )
}
