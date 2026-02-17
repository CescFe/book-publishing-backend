package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.config

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper.AuthorPersistenceMapper
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.AuthorJpaEntityRepository
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.JpaAuthorRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class JpaAuthorRepositoryTestConfig {
    @Bean
    fun authorMapper(): AuthorPersistenceMapper = AuthorPersistenceMapper()

    @Bean
    fun jpaAuthorRepository(
        authorJpaEntityRepository: AuthorJpaEntityRepository,
        authorMapper: AuthorPersistenceMapper,
    ): JpaAuthorRepository = JpaAuthorRepository(authorJpaEntityRepository, authorMapper)
}
