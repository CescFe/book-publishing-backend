package org.cescfe.bookpublishing.author.objectMothers

import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import java.util.UUID

object AuthorSummaryObjectMother {
    private const val AUTHOR_ID_CONTRACT_TEST = "477537ff-7e8b-4930-bd41-d7f3589120b1"

    fun create(
        id: UUID = UUID.randomUUID(),
        fullName: String = "Test Author",
        pseudonym: String? = null,
        email: String? = null,
    ): AuthorSummary =
        AuthorSummary(
            id = AuthorId(id),
            fullName = FullName(fullName),
            pseudonym = pseudonym?.let { Pseudonym(it) },
            email = email?.let { Email(it) },
        )

    fun createFirstAuthorSummary(): AuthorSummary =
        create(
            id = UUID.fromString(AUTHOR_ID_CONTRACT_TEST),
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            email = "tolkien@example.com",
        )

    fun createSecondAuthorSummary(): AuthorSummary =
        create(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            fullName = "Minimal Author",
        )
}
