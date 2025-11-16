package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface AuthorJpaEntityRepository : JpaRepository<AuthorEntity, UUID> {
    @Query(
        """
        SELECT DISTINCT p FROM AuthorEntity p
        JOIN p.personRoles pr
        JOIN pr.role r
        WHERE r.name = 'AUTHOR'
        ORDER BY p.fullName ASC
    """,
    )
    fun findAllAuthors(): List<AuthorEntity>

    @Query(
        """
        SELECT DISTINCT p FROM AuthorEntity p
        JOIN p.personRoles pr
        JOIN pr.role r
        WHERE r.name = 'AUTHOR'
        ORDER BY p.fullName ASC
    """,
    )
    fun findAllAuthors(pageable: Pageable): List<AuthorEntity>

    @Query(
        """
        SELECT COUNT(DISTINCT p) FROM AuthorEntity p
        JOIN p.personRoles pr
        JOIN pr.role r
        WHERE r.name = 'AUTHOR'
    """,
    )
    fun countAllAuthors(): Long

    @Query(
        """
        SELECT p FROM AuthorEntity p
        JOIN p.personRoles pr
        JOIN pr.role r
        WHERE p.id = :id
    """,
    )
    fun findAuthorById(id: UUID): Optional<AuthorEntity>

    @Query(
        """
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM AuthorEntity p
        JOIN p.personRoles pr
        JOIN pr.role r
        WHERE p.id = :id AND r.name = 'AUTHOR'
    """,
    )
    fun existsAuthorById(id: UUID): Boolean

    @Query(
        """
        SELECT p FROM AuthorEntity p
        WHERE p.email = :email
    """,
    )
    fun findByEmail(email: String): Optional<AuthorEntity>

    @Query(
        """
        SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
        FROM AuthorEntity p
        WHERE p.email = :email
    """,
    )
    fun existsByEmail(email: String): Boolean
}
