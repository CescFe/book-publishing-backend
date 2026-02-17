package org.cescfe.bookpublishing.collection.objectMothers

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel

object CreateCollectionCommandObjectMother {
    fun create(
        name: String = "Test Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
    ): CreateCollectionUseCase.Command =
        CreateCollectionUseCase.Command(
            name = name,
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages,
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres,
        )

    fun createWithAllFields(): CreateCollectionUseCase.Command =
        create(
            name = "Fantasy Classics",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.CATALAN, Language.SPANISH),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE, Genre.HISTORICAL_FICTION),
        )

    fun createMinimal(): CreateCollectionUseCase.Command =
        create(
            name = "Minimal Collection",
        )
}
