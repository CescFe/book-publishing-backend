package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import java.util.UUID

object UpdateAuthorInputValuesObjectMother {
    fun create(
        authorId: String = UUID.randomUUID().toString(),
        fullName: String = "Updated Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): UpdateAuthorUseCase.Command =
        UpdateAuthorUseCase.Command(
            authorId = authorId,
            fullName = fullName,
            pseudonym = pseudonym,
            biography = biography,
            email = email,
            website = website,
        )

    fun createWithTolkienId(): UpdateAuthorUseCase.Command =
        create(
            authorId = "477537ff-7e8b-4930-bd41-d7f3589120b1",
        )
}
