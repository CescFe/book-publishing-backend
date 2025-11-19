package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.config

import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.mapper.CollectionPersistenceMapper
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.CollectionJpaEntityRepository
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.repository.JpaCollectionRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class JpaCollectionRepositoryTestConfig {
    @Bean
    fun collectionMapper(): CollectionPersistenceMapper = CollectionPersistenceMapper()

    @Bean
    fun jpaCollectionRepository(
        collectionJpaEntityRepository: CollectionJpaEntityRepository,
        collectionMapper: CollectionPersistenceMapper,
    ): JpaCollectionRepository = JpaCollectionRepository(collectionJpaEntityRepository, collectionMapper)
}
