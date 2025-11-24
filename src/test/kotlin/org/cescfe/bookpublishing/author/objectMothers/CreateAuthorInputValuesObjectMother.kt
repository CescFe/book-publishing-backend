package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase

object CreateAuthorInputValuesObjectMother {
    fun create(
        fullName: String = "Test Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): CreateAuthorUseCase.Command =
        CreateAuthorUseCase.Command(
            fullName = fullName,
            pseudonym = pseudonym,
            biography = biography,
            email = email,
            website = website,
        )

    fun createTolkien(): CreateAuthorUseCase.Command =
        create(
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createMinimal(): CreateAuthorUseCase.Command =
        create(
            fullName = "Minimal Author",
        )

    fun createWithEmail(email: String): CreateAuthorUseCase.Command =
        create(
            fullName = "Test Author",
            email = email,
        )
}
