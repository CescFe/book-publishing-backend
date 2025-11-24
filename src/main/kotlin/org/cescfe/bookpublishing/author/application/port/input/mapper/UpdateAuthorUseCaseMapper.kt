package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
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
            pseudonym = input.pseudonym?.let { Pseudonym(it) },
            biography = input.biography?.let { Biography(it) },
            email = input.email?.let { Email(it) },
            website = input.website?.let { Website(it) },
        )
}
