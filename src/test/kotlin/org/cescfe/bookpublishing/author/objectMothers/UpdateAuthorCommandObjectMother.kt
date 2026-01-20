package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase

object UpdateAuthorCommandObjectMother {
    fun create(
        fullName: String = "Updated Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): UpdateAuthorUseCase.Command =
        UpdateAuthorUseCase.Command(
            fullName = fullName,
            pseudonym = pseudonym,
            biography = biography,
            email = email,
            website = website,
        )
}
