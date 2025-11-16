package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.springframework.stereotype.Component

@Component
class AuthorPersistenceMapper {
    fun fromDomain(author: Author): AuthorEntity {
        val entity =
            AuthorEntity(
                id = author.id.value,
                fullName = author.fullName.value,
                pseudonym = author.pseudonym?.value,
                biography = author.biography?.value,
                email = author.email?.value,
                website = author.website?.value,
            )

        return entity
    }

    fun toDomain(entity: AuthorEntity): Author =
        Author(
            id = AuthorId(entity.id),
            fullName = FullName(entity.fullName),
            pseudonym = entity.pseudonym?.let { Pseudonym(it) },
            biography = entity.biography?.let { Biography(it) },
            email = entity.email?.let { Email(it) },
            website = entity.website?.let { Website(it) },
        )

    fun toDomainSummary(entity: AuthorEntity): AuthorSummary =
        AuthorSummary(
            id = AuthorId(entity.id),
            fullName = FullName(entity.fullName),
            pseudonym = entity.pseudonym?.let { Pseudonym(it) },
            email = entity.email?.let { Email(it) },
        )
}
