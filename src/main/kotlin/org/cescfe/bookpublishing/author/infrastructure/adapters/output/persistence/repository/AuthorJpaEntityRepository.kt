package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorEntity
import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.AuthorSummaryProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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
        SELECT p.id as id, p.fullName as fullName, p.pseudonym as pseudonym,
               p.email as email, p.version as version, pr.role.name as role
        FROM AuthorEntity p
        JOIN p.personRoles pr
        WHERE p.id IN (
            SELECT DISTINCT p2.id
            FROM AuthorEntity p2
            JOIN p2.personRoles pr2
            JOIN pr2.role r2
            WHERE r2.name = 'AUTHOR'
        )
        ORDER BY p.fullName ASC, pr.role.name ASC
    """,
    )
    fun findAllAuthorsSummary(): List<AuthorSummaryProjection>

    @Query(
        """
        SELECT p.id as id, p.fullName as fullName, p.pseudonym as pseudonym,
               p.email as email, p.version as version, pr.role.name as role
        FROM AuthorEntity p
        JOIN p.personRoles pr
        WHERE p.id IN (
            SELECT DISTINCT p2.id
            FROM AuthorEntity p2
            JOIN p2.personRoles pr2
            JOIN pr2.role r2
            WHERE r2.name = 'AUTHOR'
        )
        ORDER BY p.fullName ASC, pr.role.name ASC
    """,
    )
    fun findAllAuthorsSummary(pageable: Pageable): List<AuthorSummaryProjection>

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
        WHERE p.id = :id AND r.name = 'AUTHOR'
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

    @Modifying
    @Query(
        """
        DELETE FROM PersonRoleEntity pr
        WHERE pr.person.id = :personId
        AND pr.role.name = 'AUTHOR'
    """,
    )
    fun removeAuthorRole(
        @Param("personId") personId: UUID,
    ): Int
}
