package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase

object CreateAuthorInputValuesObjectMother {
    fun create(
        fullName: String = "Test Author",
        roles: Set<String> = setOf("AUTHOR"),
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): CreateAuthorUseCase.InputValues =
        CreateAuthorUseCase.InputValues(
            fullName = fullName,
            roles = roles,
            pseudonym = pseudonym,
            biography = biography,
            email = email,
            website = website,
        )

    fun createTolkien(): CreateAuthorUseCase.InputValues =
        create(
            fullName = "J.R.R. Tolkien",
            roles = setOf("AUTHOR"),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createMinimal(): CreateAuthorUseCase.InputValues =
        create(
            fullName = "Minimal Author",
            roles = setOf("AUTHOR"),
        )

    fun createWithEmail(email: String): CreateAuthorUseCase.InputValues =
        create(
            fullName = "Test Author",
            roles = setOf("AUTHOR"),
            email = email,
        )

    fun createWithMultipleRoles(): CreateAuthorUseCase.InputValues =
        create(
            fullName = "Multi-role Author",
            roles = setOf("AUTHOR", "ILLUSTRATOR"),
        )
}
