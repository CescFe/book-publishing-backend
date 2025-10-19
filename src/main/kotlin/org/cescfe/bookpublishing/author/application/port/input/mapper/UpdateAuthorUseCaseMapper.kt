package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.springframework.stereotype.Component

@Component
class UpdateAuthorUseCaseMapper {
    fun toDomain(
        input: UpdateAuthorUseCase.InputValues,
        existingAuthor: Author,
    ): Author =
        Author(
            id = existingAuthor.id,
            fullName = FullName(input.fullName),
            roles = input.roles.map { AuthorRole.fromString(it) }.toSet(),
            pseudonym = input.pseudonym?.let { Pseudonym(it) },
            biography = input.biography?.let { Biography(it) },
            email = input.email?.let { Email(it) },
            website = input.website?.let { Website(it) },
            version = existingAuthor.version + 1L,
        )

    fun toInputValues(author: Author): UpdateAuthorUseCase.InputValues =
        UpdateAuthorUseCase.InputValues(
            authorId = author.id.value.toString(),
            fullName = author.fullName.value,
            roles = author.roles.map { it.value }.toSet(),
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value,
            version = author.version,
        )
}
