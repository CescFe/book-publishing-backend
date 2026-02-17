package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.projection.AuthorSummaryProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthorJpaEntityRepository : JpaRepository<AuthorEntity, UUID> {
    fun findByEmail(email: String): AuthorEntity?

    fun existsByEmail(email: String): Boolean

    fun findAllProjectedByOrderByFullNameAsc(): List<AuthorSummaryProjection>

    fun findAllProjectedByOrderByFullNameAsc(pageable: Pageable): List<AuthorSummaryProjection>
}
