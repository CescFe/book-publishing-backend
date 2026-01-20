package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.springframework.stereotype.Component

@Component
class UpdateCollectionUseCaseMapper {
    fun toDomain(
        command: UpdateCollectionUseCase.Command,
        existingCollection: Collection,
    ): Collection =
        Collection(
            id = existingCollection.id,
            name = CollectionName(command.name),
            readingLevel = command.readingLevel,
            primaryLanguage = command.primaryLanguage,
            secondaryLanguages = command.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = command.primaryGenre,
            secondaryGenres = command.secondaryGenres?.let(::SecondaryGenres),
        )
}
