package org.cescfe.bookpublishing.collection.domain.model

import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel

data class CollectionSummary(
    val id: CollectionId,
    val name: CollectionName,
    val readingLevel: ReadingLevel? = null,
    val primaryLanguage: Language? = null,
    val primaryGenre: Genre? = null,
)
