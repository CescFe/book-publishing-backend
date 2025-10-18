package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.entity

import java.util.UUID

interface AuthorSummaryProjection {
    fun getId(): UUID

    fun getFullName(): String

    fun getPseudonym(): String?

    fun getEmail(): String?

    fun getRole(): String
}
