package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface GetAuthorUseCase {
    fun execute(query: Query): Author

    data class Query(
        val authorId: String,
    )
}
