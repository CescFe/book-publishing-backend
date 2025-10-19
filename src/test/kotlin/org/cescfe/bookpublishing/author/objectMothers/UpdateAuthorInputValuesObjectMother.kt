package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import java.util.UUID

object UpdateAuthorInputValuesObjectMother {
    fun create(
        authorId: String = UUID.randomUUID().toString(),
        fullName: String = "Updated Author",
        roles: Set<String> = setOf("AUTHOR"),
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
        version: Long,
    ): UpdateAuthorUseCase.InputValues =
        UpdateAuthorUseCase.InputValues(
            authorId = authorId,
            fullName = fullName,
            roles = roles,
            pseudonym = pseudonym,
            biography = biography,
            email = email,
            website = website,
            version = version,
        )

    fun createWithTolkienId(): UpdateAuthorUseCase.InputValues =
        create(
            authorId = "477537ff-7e8b-4930-bd41-d7f3589120b1",
            version = 1L,
        )

    fun createWithMultipleRoles(): UpdateAuthorUseCase.InputValues =
        create(
            roles = setOf("AUTHOR", "ILLUSTRATOR"),
            version = 1L,
        )
}
