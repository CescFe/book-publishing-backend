package org.cescfe.bookpublishing.author.application.port.input

interface DeleteAuthorUseCase {
    fun execute(input: InputValues)

    data class InputValues(
        val authorId: String,
    )
}
