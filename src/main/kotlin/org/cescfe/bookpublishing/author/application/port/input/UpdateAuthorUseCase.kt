package org.cescfe.bookpublishing.author.application.port.input

import org.cescfe.bookpublishing.author.domain.model.Author

interface UpdateAuthorUseCase {
    fun execute(command: Command): Author

    data class Command(
        val authorId: String,
        val fullName: String,
        val pseudonym: String? = null,
        val biography: String? = null,
        val email: String? = null,
        val website: String? = null,
    )
}
