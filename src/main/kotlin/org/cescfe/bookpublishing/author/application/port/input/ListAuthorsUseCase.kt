package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult

interface ListAuthorsUseCase {
    fun execute(input: InputValues): PaginatedResult<Author>

    data class InputValues(
        val page: Int,
        val limit: Int,
    )
}
