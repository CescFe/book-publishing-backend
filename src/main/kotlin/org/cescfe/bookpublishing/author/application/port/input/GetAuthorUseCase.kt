package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface GetAuthorUseCase {
    fun execute(input: InputValues): Author

    data class InputValues(
        val authorId: String,
    )
}
