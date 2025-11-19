package org.cescfe.bookpublishing.collection.objectMothers

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import java.util.UUID

object CollectionObjectMother {
    fun create(
        id: UUID = UUID.randomUUID(),
        name: String = "Test Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
    ): Collection =
        Collection(
            id = CollectionId(id),
            name = CollectionName(name),
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages?.let { SecondaryLanguages(it) },
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres?.let { SecondaryGenres(it) },
        )

    fun createWithAllFields(): Collection =
        create(
            name = "Fantasy Classics",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
        )

    fun createMinimal(): Collection =
        create(
            name = "Minimal Collection",
        )
}
