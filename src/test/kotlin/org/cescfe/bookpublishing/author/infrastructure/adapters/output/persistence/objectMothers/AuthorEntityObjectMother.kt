package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.objectMothers

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleId
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.RoleEntity
import java.util.UUID

object AuthorEntityObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        version: Long = 1L,
        fullName: String = "Test Author",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null,
        personRoles: List<RoleEntity> = listOf(RoleEntityObjectMother.createAuthorRole()),
    ): AuthorEntity {
        val entity =
            AuthorEntity(
                id = id,
                version = version,
                fullName = fullName,
                pseudonym = pseudonym,
                biography = biography,
                email = email,
                website = website,
            )

        personRoles.forEach { role ->
            entity.personRoles.add(
                PersonRoleEntity(
                    id = PersonRoleId(id, role.id),
                    person = entity,
                    role = role,
                ),
            )
        }

        return entity
    }

    fun createTolkien(): AuthorEntity =
        create(
            fullName = "J.R.R. Tolkien",
            pseudonym = "John Ronald Reuel Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://tolkiensociety.org",
            personRoles =
                listOf(
                    RoleEntityObjectMother.createAuthorRole(),
                    RoleEntityObjectMother.createIllustratorRole(),
                ),
        )

    fun createSimple(): AuthorEntity =
        create(
            fullName = "Simple Author",
            personRoles = listOf(RoleEntityObjectMother.createAuthorRole()),
        )
}
