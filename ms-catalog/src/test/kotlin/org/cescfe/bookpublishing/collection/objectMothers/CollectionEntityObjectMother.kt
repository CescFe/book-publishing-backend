package org.cescfe.bookpublishing.collection.objectMothers

import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity.CollectionEntity
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.util.UUID

object CollectionEntityObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        name: String = "Test Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
    ): CollectionEntity {
        val entity =
            CollectionEntity(
                id = id,
                name = name,
                readingLevel = readingLevel,
                primaryLanguage = primaryLanguage,
                secondaryLanguages = secondaryLanguages,
                primaryGenre = primaryGenre,
                secondaryGenres = secondaryGenres,
            )
        return entity
    }

    fun createWithAllFields(): CollectionEntity =
        create(
            name = "Fantasy Classics",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
        )

    fun createMinimal(): CollectionEntity =
        create(
            name = "Simple Collection",
        )
}
