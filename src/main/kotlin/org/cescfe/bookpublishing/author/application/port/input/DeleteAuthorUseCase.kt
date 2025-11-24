package org.cescfe.bookpublishing.author.application.port.input

interface DeleteAuthorUseCase {
    fun execute(command: Command)

    data class Command(
        val authorId: String,
    )
}
