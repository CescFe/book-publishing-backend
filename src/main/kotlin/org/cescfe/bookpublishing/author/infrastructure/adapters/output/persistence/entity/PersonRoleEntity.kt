package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleId
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.RoleEntity

@Entity
@Table(name = "person_role", schema = "publishing")
data class PersonRoleEntity(
    @EmbeddedId
    val id: PersonRoleId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    val person: AuthorEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    val role: RoleEntity,
)
