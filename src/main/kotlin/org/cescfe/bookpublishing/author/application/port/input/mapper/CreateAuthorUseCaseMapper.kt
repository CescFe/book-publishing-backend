package org.cescfe.bookpublishing.author.application.port.input.mapper

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.springframework.stereotype.Component

@Component
class CreateAuthorUseCaseMapper {
    fun toDomain(input: CreateAuthorUseCase.InputValues): Author =
        Author(
            id = AuthorId.generate(),
            fullName = FullName(input.fullName),
            pseudonym = input.pseudonym?.let { Pseudonym(it) },
            biography = input.biography?.let { Biography(it) },
            email = input.email?.let { Email(it) },
            website = input.website?.let { Website(it) },
        )

    fun toInputValues(author: Author): CreateAuthorUseCase.InputValues =
        CreateAuthorUseCase.InputValues(
            fullName = author.fullName.value,
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value,
        )
}
