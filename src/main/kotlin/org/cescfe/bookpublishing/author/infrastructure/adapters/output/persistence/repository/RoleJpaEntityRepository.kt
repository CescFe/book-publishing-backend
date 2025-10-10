package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.repository

import org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleJpaEntityRepository : JpaRepository<RoleEntity, Long> {
    fun findByName(name: String): RoleEntity?
}
