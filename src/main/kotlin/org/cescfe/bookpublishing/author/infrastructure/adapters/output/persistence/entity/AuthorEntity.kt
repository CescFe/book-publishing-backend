package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.PersonRoleEntity
import org.cescfe.bookpublishing.shared.infrastructure.adapters.output.AuditableEntity
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "person", schema = "publishing")
data class AuthorEntity(
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    val id: UUID,
    @Version
    @Column(name = "version")
    val version: Long = 1,
    @Column(name = "full_name", nullable = false)
    val fullName: String,
    @Column(name = "pseudonym")
    val pseudonym: String?,
    @Column(name = "biography", columnDefinition = "TEXT")
    val biography: String?,
    @Column(name = "email")
    val email: String?,
    @Column(name = "website")
    val website: String?,
    @OneToMany(mappedBy = "person", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val personRoles: MutableList<PersonRoleEntity> = mutableListOf(),
) : AuditableEntity()
