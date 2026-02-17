package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.projection

import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.util.UUID

interface CollectionSummaryProjection {
    val id: UUID
    val name: String
    val readingLevel: ReadingLevel?
    val primaryLanguage: Language?
    val primaryGenre: Genre?
}
