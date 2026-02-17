package org.cescfe.bookpublishing.collection.objectMothers

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel

object UpdateCollectionCommandObjectMother {
    fun create(
        name: String = "Updated Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language>? = null,
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre>? = null,
    ): UpdateCollectionUseCase.Command =
        UpdateCollectionUseCase.Command(
            name = name,
            readingLevel = readingLevel,
            primaryLanguage = primaryLanguage,
            secondaryLanguages = secondaryLanguages,
            primaryGenre = primaryGenre,
            secondaryGenres = secondaryGenres,
        )
}
