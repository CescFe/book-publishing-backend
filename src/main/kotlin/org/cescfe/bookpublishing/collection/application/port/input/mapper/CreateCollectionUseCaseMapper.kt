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
    fun toDomain(command: CreateCollectionUseCase.Command): Collection =
        Collection(
            id = CollectionId.generate(),
            name = CollectionName(command.name),
            readingLevel = command.readingLevel,
            primaryLanguage = command.primaryLanguage,
            secondaryLanguages = command.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = command.primaryGenre,
            secondaryGenres = command.secondaryGenres?.let(::SecondaryGenres),
        )
}
