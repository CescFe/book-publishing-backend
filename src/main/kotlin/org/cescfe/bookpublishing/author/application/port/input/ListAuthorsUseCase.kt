package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult

interface ListAuthorsUseCase {
    fun execute(input: InputValues): PaginatedResult<AuthorSummary>

    data class InputValues(
        val page: Int,
        val limit: Int,
    )
}
