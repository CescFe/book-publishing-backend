package org.cescfe.bookpublishing.author.infrastructure.adapters.output.persistence.projection

import java.util.UUID

interface AuthorSummaryProjection {
    val id: UUID
    val fullName: String
    val pseudonym: String?
    val email: String?
}
