package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface UpdateAuthorUseCase {
    fun execute(input: InputValues): Author

    data class InputValues(
        val authorId: String,
        val fullName: String,
        val roles: Set<String>,
        val pseudonym: String? = null,
        val biography: String? = null,
        val email: String? = null,
        val website: String? = null,
    )
}
