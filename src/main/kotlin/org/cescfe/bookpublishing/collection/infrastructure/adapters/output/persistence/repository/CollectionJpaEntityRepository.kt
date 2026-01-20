package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity.CollectionEntity
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.projection.CollectionSummaryProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CollectionJpaEntityRepository : JpaRepository<CollectionEntity, UUID> {
    fun findByName(name: String): CollectionEntity?

    fun existsByName(name: String): Boolean

    fun findAllProjectedByOrderByNameAsc(): List<CollectionSummaryProjection>

    fun findAllProjectedByOrderByNameAsc(pageable: Pageable): List<CollectionSummaryProjection>
}
