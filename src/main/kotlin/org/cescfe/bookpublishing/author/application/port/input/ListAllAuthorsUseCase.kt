package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult

interface ListAllAuthorsUseCase {
    fun execute(): NonPaginatedResult<AuthorSummary>
}
