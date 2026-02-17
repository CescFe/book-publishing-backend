package org.cescfe.bookpublishing.book.application.port.input

interface DeleteBookUseCase {
    fun execute(command: Command)

    data class Command(
        val bookId: String,
    )
}
