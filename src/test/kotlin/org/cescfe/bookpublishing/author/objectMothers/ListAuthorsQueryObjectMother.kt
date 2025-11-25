package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase

object ListAuthorsQueryObjectMother {
    fun create(
        page: Int = 1,
        limit: Int = 20,
    ): ListAuthorsUseCase.Query =
        ListAuthorsUseCase.Query(
            page = page,
            limit = limit,
        )
}
