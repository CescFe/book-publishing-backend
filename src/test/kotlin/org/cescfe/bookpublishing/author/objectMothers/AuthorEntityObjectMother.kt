package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import java.util.UUID

object AuthorEntityObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        fullName: String = "Test Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): AuthorEntity {
        val entity =
            AuthorEntity(
                id = id,
                fullName = fullName,
                pseudonym = pseudonym,
                biography = biography,
                email = email,
                website = website,
            )
        return entity
    }

    fun createTolkien(): AuthorEntity =
        create(
            fullName = "J.R.R. Tolkien",
            pseudonym = "John Ronald Reuel Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://tolkiensociety.org",
        )

    fun createSimple(): AuthorEntity =
        create(
            fullName = "Simple Author",
        )
}
