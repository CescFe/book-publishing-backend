package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.AuditableEntity
import java.util.UUID

@Entity
@Table(name = "author", schema = "publishing")
data class AuthorEntity(
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    val id: UUID,
    @Column(name = "full_name", nullable = false)
    val fullName: String,
    @Column(name = "pseudonym")
    val pseudonym: String?,
    @Column(name = "biography")
    val biography: String?,
    @Column(name = "email")
    val email: String?,
    @Column(name = "website")
    val website: String?,
) : AuditableEntity()
