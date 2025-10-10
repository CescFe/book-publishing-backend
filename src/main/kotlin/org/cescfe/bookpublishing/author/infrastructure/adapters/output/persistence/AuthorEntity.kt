package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.Version
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

    @Column(name = "roles", nullable = false)
    val roles: String,
) {
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: Timestamp

    @Column(name = "updated_at", nullable = false, updatable = false)
    lateinit var updatedAt: Timestamp

    @PrePersist
    fun prePersist() {
        this.createdAt = Timestamp.from(Instant.now())
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = Timestamp.from(Instant.now())
    }
}
