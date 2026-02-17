package org.cescfe.bookpublishing.collection.objectMothers

import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.util.UUID

object CollectionSummaryObjectMother {
    private const val COLLECTION_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"

    fun create(
        id: UUID = UUID.randomUUID(),
        name: String = "Test Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        primaryGenre: Genre? = null,
    ): CollectionSummary =
        CollectionSummary(
            id = CollectionId(id),
            name = CollectionName(name),
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            primaryGenre = primaryGenre,
        )

    fun createFirstCollectionSummary(): CollectionSummary =
        create(
            id = UUID.fromString(COLLECTION_ID),
            name = "Fantasy Classics",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            primaryGenre = Genre.FANTASY,
        )

    fun createSecondCollectionSummary(): CollectionSummary =
        create(
            id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
            name = "Minimal Collection",
        )
}
