package org.cescfe.bookpublishing.collection.application.port.input

import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CollectionDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel

interface CreateCollectionUseCase {
    fun execute(input: InputValues): CollectionDTO

    data class InputValues(
        val name: String,
        val readingLevel: ReadingLevel? = null,
        val primaryLanguage: Language? = null,
        val secondaryLanguages: List<Language>? = null,
        val primaryGenre: Genre? = null,
        val secondaryGenres: List<Genre>? = null,
    )
}
