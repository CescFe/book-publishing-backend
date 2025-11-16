package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface CreateAuthorUseCase {
    fun execute(input: InputValues): Author

    data class InputValues(
        val fullName: String,
        val pseudonym: String? = null,
        val biography: String? = null,
        val email: String? = null,
        val website: String? = null,
    )
}
