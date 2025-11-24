package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import java.util.UUID

object DeleteAuthorInputValuesObjectMother {
    fun create(authorId: String = UUID.randomUUID().toString()): DeleteAuthorUseCase.Command =
        DeleteAuthorUseCase.Command(authorId = authorId)

    fun createWithTolkienId(): DeleteAuthorUseCase.Command = create("477537ff-7e8b-4930-bd41-d7f3589120b1")
}
