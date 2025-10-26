package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import java.util.UUID

object AuthorObjectMother {
    private const val AUTHOR_ID_CONTROLLER_IT = "123e4567-e89b-12d3-a456-426614174000"

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

    fun createMinimal(): Author =
        create(
            fullName = "Minimal Author",
            roles = setOf(AuthorRole.AUTHOR),
        )

    fun createWithEmail(email: String): Author =
        create(
            fullName = "Test Author",
            roles = setOf(AuthorRole.AUTHOR),
            email = email,
        )

    // ==============
    // Controller IT
    // ==============
    fun createWithMultipleRoles(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTROLLER_IT),
            fullName = "J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createForGetContractTest(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTROLLER_IT),
            fullName = "J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createForUpdateContractTest(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTROLLER_IT),
            fullName = "Updated J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Updated Tolkien",
            biography = "Updated English writer and philologist",
            email = "updated.tolkien@example.com",
            website = "https://www.updated-tolkiensociety.org",
        )
}
