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
    fun toDomain(input: CreateAuthorUseCase.Command): Author =
        Author(
            id = AuthorId.generate(),
            fullName = FullName(input.fullName),
            pseudonym = input.pseudonym?.let(::Pseudonym),
            biography = input.biography?.let(::Biography),
            email = input.email?.let(::Email),
            website = input.website?.let(::Website),
        )
}
