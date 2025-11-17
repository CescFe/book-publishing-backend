package org.cescfe.bookpublishing.collection.domain.port

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId

interface CollectionRepositoryView {
    fun findById(id: CollectionId): Collection?

    fun countAll(): Long

    fun save(collection: Collection): Collection

    fun deleteById(id: CollectionId)

    fun existsById(id: CollectionId): Boolean

    fun findByName(name: String): Collection?
}
