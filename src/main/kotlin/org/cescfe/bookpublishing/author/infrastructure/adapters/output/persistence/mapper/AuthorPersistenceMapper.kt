package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.AuthorId
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.Biography
import org.cescfe.bookpublishing.author.domain.model.Email
import org.cescfe.bookpublishing.author.domain.model.FullName
import org.cescfe.bookpublishing.author.domain.model.Pseudonym
import org.cescfe.bookpublishing.author.domain.model.Website
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleId
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.exception.RoleNotFoundException
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.RoleJpaEntityRepository
import org.springframework.stereotype.Component

@Component
class AuthorPersistenceMapper(
    private val roleJpaEntityRepository: RoleJpaEntityRepository,
) {
    fun fromDomain(author: Author): AuthorEntity {
        val entity =
            AuthorEntity(
                id = author.id.value,
                fullName = author.fullName.value,
                pseudonym = author.pseudonym?.value,
                biography = author.biography?.value,
                email = author.email?.value,
                website = author.website?.value,
                version = author.version,
            )

        author.roles.forEach { role ->
            val roleEntity =
                roleJpaEntityRepository.findByName(role.value)
                    ?: throw RoleNotFoundException.forRoleName(role.value)

            val personRole =
                PersonRoleEntity(
                    id = PersonRoleId(author.id.value, roleEntity.id),
                    person = entity,
                    role = roleEntity,
                )
            entity.personRoles.add(personRole)
        }

        return entity
    }

    fun toDomain(entity: AuthorEntity): Author {
        val rolesSet = entity.personRoles.map { AuthorRole.fromString(it.role.name) }.toSet()

        return Author(
            id = AuthorId(entity.id),
            fullName = FullName(entity.fullName),
            roles = rolesSet,
            pseudonym = entity.pseudonym?.let { Pseudonym(it) },
            biography = entity.biography?.let { Biography(it) },
            email = entity.email?.let { Email(it) },
            website = entity.website?.let { Website(it) },
            version = entity.version,
        )
    }

    fun toDomainSummary(entity: AuthorEntity): AuthorSummary {
        val rolesSet = entity.personRoles.map { AuthorRole.fromString(it.role.name) }.toSet()

        return AuthorSummary(
            id = AuthorId(entity.id),
            fullName = FullName(entity.fullName),
            roles = rolesSet,
            pseudonym = entity.pseudonym?.let { Pseudonym(it) },
            email = entity.email?.let { Email(it) },
            version = entity.version,
        )
    }
}
