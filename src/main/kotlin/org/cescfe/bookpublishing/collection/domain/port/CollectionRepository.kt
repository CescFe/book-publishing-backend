package org.cescfe.bookpublishing.collection.domain.port

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary

interface CollectionRepository {
    fun findById(id: CollectionId): Collection?

    fun findAllSummary(): List<CollectionSummary>

    fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<CollectionSummary>

    fun countAll(): Long

    fun save(collection: Collection): Collection

    fun deleteById(id: CollectionId)

    fun existsById(id: CollectionId): Boolean

    fun findByName(name: String): Collection?

    fun existsByName(name: String): Boolean
}
