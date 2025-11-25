package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import java.util.UUID

object AuthorObjectMother {
    private const val AUTHOR_ID_CONTROLLER_IT = "123e4567-e89b-12d3-a456-426614174000"

    fun create(
        id: UUID = UUID.randomUUID(),
        fullName: String = "Test Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
        audit: Metadata? = null,
    ): Author =
        Author(
            id = AuthorId(id),
            fullName = FullName(fullName),
            pseudonym = pseudonym?.let { Pseudonym(it) },
            biography = biography?.let { Biography(it) },
            email = email?.let { Email(it) },
            website = website?.let { Website(it) },
            audit = audit,
        )

    fun createWithAllFields(): Author =
        create(
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createMinimal(): Author =
        create(
            fullName = "Minimal Author",
        )

    // ==============
    // Controller IT
    // ==============
    fun createForControllerIT(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTROLLER_IT),
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )
}
