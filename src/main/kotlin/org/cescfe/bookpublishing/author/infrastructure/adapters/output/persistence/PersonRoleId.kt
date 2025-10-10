package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
data class PersonRoleId(
    @Column(name = "person_id")
    val personId: UUID,
    @Column(name = "role_id")
    val roleId: Long,
)
