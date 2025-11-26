package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import java.util.UUID

interface BookSummaryProjection {
    val id: UUID
    val title: String
    val authorId: UUID
    val collectionId: UUID
    val basePrice: Double
    val isbn: String?
    val status: Status?
}
