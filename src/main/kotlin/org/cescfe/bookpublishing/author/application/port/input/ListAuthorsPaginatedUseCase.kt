package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult

interface ListAuthorsPaginatedUseCase {
    fun execute(query: Query): PaginatedResult<AuthorSummary>

    data class Query(
        val page: Int,
        val limit: Int,
    )
}
