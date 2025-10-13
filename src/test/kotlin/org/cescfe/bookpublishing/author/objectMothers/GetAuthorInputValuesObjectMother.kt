package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import java.util.UUID

object GetAuthorInputValuesObjectMother {
    fun create(authorId: String = UUID.randomUUID().toString()): GetAuthorUseCase.InputValues =
        GetAuthorUseCase.InputValues(authorId = authorId)

    fun createWithAuthorId(authorId: AuthorId): GetAuthorUseCase.InputValues = create(authorId.value.toString())

    fun createWithTolkienId(): GetAuthorUseCase.InputValues = create("477537ff-7e8b-4930-bd41-d7f3589120b1")
}
