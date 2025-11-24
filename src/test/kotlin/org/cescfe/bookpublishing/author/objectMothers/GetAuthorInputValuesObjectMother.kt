package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import java.util.UUID

object GetAuthorInputValuesObjectMother {
    fun create(authorId: String = UUID.randomUUID().toString()): GetAuthorUseCase.Query =
        GetAuthorUseCase.Query(authorId = authorId)

    fun createWithTolkienId(): GetAuthorUseCase.Query = create("477537ff-7e8b-4930-bd41-d7f3589120b1")
}
