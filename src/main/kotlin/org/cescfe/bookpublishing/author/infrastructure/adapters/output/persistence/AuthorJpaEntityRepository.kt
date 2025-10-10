package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface AuthorJpaEntityRepository : JpaRepository<AuthorEntity, UUID> {
    @Query("SELECT a FROM AuthorEntity a WHERE a.roles LIKE '%AUTHOR%'")
    fun findAllAuthors(): List<AuthorEntity>

    @Query("SELECT a FROM AuthorEntity a WHERE a.id = :id AND a.roles LIKE '%AUTHOR%'")
    fun findAuthorById(id: UUID): Optional<AuthorEntity>

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuthorEntity a WHERE a.id = :id AND a.roles LIKE '%AUTHOR%'")
    fun existsAuthorById(id: UUID): Boolean
}
