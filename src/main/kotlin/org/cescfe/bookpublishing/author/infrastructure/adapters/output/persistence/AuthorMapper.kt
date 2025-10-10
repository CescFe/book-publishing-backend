package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.springframework.stereotype.Component

@Component
class AuthorMapper {

    fun fromDomain(author: Author): AuthorEntity {
        return AuthorEntity(
            id = author.id.value,
            fullName = author.fullName.value,
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value,
            roles = author.roles.joinToString(",") { it.value }
        )
    }

    fun toDomain(entity: AuthorEntity): Author {
        val rolesSet = entity.roles.split(",")
            .map { it.trim() }
            .map { AuthorRole.fromString(it) }
            .toSet()

        return Author(
            id = AuthorId(entity.id),
            fullName = FullName(entity.fullName),
            roles = rolesSet,
            pseudonym = entity.pseudonym?.let { Pseudonym(it) },
            biography = entity.biography?.let { Biography(it) },
            email = entity.email?.let { Email(it) },
            website = entity.website?.let { Website(it) }
        )
    }
}
