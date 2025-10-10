package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PersonRoleJpaRepository : JpaRepository<PersonRoleEntity, PersonRoleId> {
    @Query("""
        SELECT pr FROM PersonRoleEntity pr
        WHERE pr.id.personId = :personId AND pr.id.roleId = :roleId
    """)
    fun findByPersonIdAndRoleId(personId: UUID, roleId: Long): List<PersonRoleEntity>

    fun deleteByPersonId(personId: UUID)
}
