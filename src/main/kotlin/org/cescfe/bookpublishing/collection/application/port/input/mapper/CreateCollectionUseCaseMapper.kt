package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.springframework.stereotype.Component

@Component
class CreateCollectionUseCaseMapper {
    fun toDomain(input: CreateCollectionUseCase.InputValues): Collection =
        Collection(
            id = CollectionId.generate(),
            name = CollectionName(input.name),
            readingLevel = input.readingLevel,
            primaryLanguage = input.primaryLanguage,
            secondaryLanguages = input.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = input.primaryGenre,
            secondaryGenres = input.secondaryGenres?.let { SecondaryGenres(it) },
        )
}
