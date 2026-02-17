package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel

interface UpdateCollectionUseCase {
    fun execute(
        collectionId: String,
        command: Command,
    ): Collection

    data class Command(
        val name: String,
        val readingLevel: ReadingLevel? = null,
        val primaryLanguage: Language? = null,
        val secondaryLanguages: List<Language>? = null,
        val primaryGenre: Genre? = null,
        val secondaryGenres: List<Genre>? = null,
    )
}
