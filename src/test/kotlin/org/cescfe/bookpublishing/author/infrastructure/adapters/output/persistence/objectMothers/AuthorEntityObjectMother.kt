package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import java.util.UUID

object AuthorEntityObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        fullName: String = "Test Author",
        roles: Set<AuthorRole> = setOf(AuthorRole.AUTHOR),
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
    ): Author =
        Author(
            id = AuthorId(id),
            fullName = FullName(fullName),
            roles = roles,
            pseudonym = pseudonym?.let { Pseudonym(it) },
            biography = biography?.let { Biography(it) },
            email = email?.let { Email(it) },
            website = website?.let { Website(it) },
        )

    fun createTolkien(): Author =
        create(
            fullName = "J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )
}
