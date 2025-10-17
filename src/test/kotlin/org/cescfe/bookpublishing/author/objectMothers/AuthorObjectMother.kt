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
    private const val AUTHOR_ID_CONTRACT_TEST = "477537ff-7e8b-4930-bd41-d7f3589120b1"

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
    // Contract Tests
    // ==============
    fun createWithMultipleRoles(): Author =
        create(
            fullName = "J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createForGetContractTest(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTRACT_TEST),
            fullName = "J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkiensociety.org",
        )

    fun createForUpdateContractTest(): Author =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTRACT_TEST),
            fullName = "Updated J.R.R. Tolkien",
            roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
            pseudonym = "Updated Tolkien",
            biography = "Updated English writer and philologist",
            email = "updated.tolkien@example.com",
            website = "https://www.updated-tolkiensociety.org",
        )

    fun createForGetAllContractTest(): Author =
        create(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            fullName = "Minimal Author",
            roles = setOf(AuthorRole.AUTHOR),
        )
}
