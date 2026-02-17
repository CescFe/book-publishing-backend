package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository.config

import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.mapper.BookPersistenceMapper
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository.BookJpaEntityRepository
import org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.repository.JpaBookRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class JpaBookRepositoryTestConfig {
    @Bean
    fun bookMapper(): BookPersistenceMapper = BookPersistenceMapper()

    @Bean
    fun jpaBookRepository(
        bookJpaEntityRepository: BookJpaEntityRepository,
        bookMapper: BookPersistenceMapper,
    ): JpaBookRepository = JpaBookRepository(bookJpaEntityRepository, bookMapper)
}
