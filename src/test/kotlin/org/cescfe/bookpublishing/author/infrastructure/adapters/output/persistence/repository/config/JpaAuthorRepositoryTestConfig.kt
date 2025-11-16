package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.config

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.mapper.AuthorPersistenceMapper
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.AuthorJpaEntityRepository
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository.JpaAuthorRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class JpaAuthorRepositoryTestConfig {
    @Bean
    @Primary
    fun authorMapper(): AuthorPersistenceMapper = AuthorPersistenceMapper()

    @Bean
    @Primary
    fun jpaAuthorRepository(
        authorJpaEntityRepository: AuthorJpaEntityRepository,
        authorMapper: AuthorPersistenceMapper,
    ): JpaAuthorRepository = JpaAuthorRepository(authorJpaEntityRepository, authorMapper)
}
