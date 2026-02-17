package org.cescfe.bookpublishing.book.infrastructure.adapters.output.persistence.projection

import org.cescfe.bookpublishing.book.domain.model.enum.Status
import java.util.UUID

interface BookSummaryProjection {
    val id: UUID
    val title: String
    val authorId: UUID
    val authorName: String
    val collectionId: UUID
    val collectionName: String
    val basePrice: Double
    val finalPrice: Double
    val isbn: String?
    val status: Status?
}
