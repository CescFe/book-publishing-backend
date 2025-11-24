package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.port.CollectionRepository
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.mapper.CollectionPersistenceMapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class JpaCollectionRepository(
    private val collectionJpaEntityRepository: CollectionJpaEntityRepository,
    private val collectionMapper: CollectionPersistenceMapper,
) : CollectionRepository {
    override fun findById(id: CollectionId): Collection? =
        collectionJpaEntityRepository
            .findById(id.value)
            .map { collectionMapper.toDomain(it) }
            .orElse(null)

    override fun findAllSummary(): List<CollectionSummary> =
        collectionJpaEntityRepository
            .findAllProjectedByOrderByNameAsc()
            .map { collectionMapper.toDomainSummary(it) }

    override fun findAllSummary(
        page: Int,
        limit: Int,
    ): List<CollectionSummary> {
        val pageable: Pageable = PageRequest.of(page - 1, limit)
        return collectionJpaEntityRepository
            .findAllProjectedByOrderByNameAsc(pageable)
            .map { collectionMapper.toDomainSummary(it) }
    }

    override fun countAll(): Long = collectionJpaEntityRepository.count()

    override fun save(collection: Collection): Collection {
        val entity = collectionMapper.fromDomain(collection)
        return collectionMapper.toDomain(collectionJpaEntityRepository.save(entity))
    }

    override fun deleteById(id: CollectionId) {
        collectionJpaEntityRepository.deleteById(id.value)
    }

    override fun existsById(id: CollectionId): Boolean = collectionJpaEntityRepository.existsById(id.value)

    override fun findByName(name: String): Collection? =
        collectionJpaEntityRepository
            .findByName(name)
            ?.let { entity -> collectionMapper.toDomain(entity) }
}
