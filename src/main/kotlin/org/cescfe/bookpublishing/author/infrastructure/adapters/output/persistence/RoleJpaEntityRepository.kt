package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleJpaEntityRepository : JpaRepository<RoleEntity, Long> {
    fun findByName(name: String): RoleEntity?
}
