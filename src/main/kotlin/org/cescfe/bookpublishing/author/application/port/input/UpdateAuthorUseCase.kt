package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface UpdateAuthorUseCase {
    fun execute(
        authorId: String,
        command: Command,
    ): Author

    data class Command(
        val fullName: String,
        val pseudonym: String? = null,
        val biography: String? = null,
        val email: String? = null,
        val website: String? = null,
    )
}
