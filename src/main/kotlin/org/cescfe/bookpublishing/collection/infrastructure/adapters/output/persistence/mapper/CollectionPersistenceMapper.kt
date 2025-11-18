package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity.CollectionEntity
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.springframework.stereotype.Component
import java.util.List

@Component
class CollectionPersistenceMapper {
    fun fromDomain(collection: Collection): CollectionEntity =
        CollectionEntity(
            id = collection.id.value,
            name = collection.name.value,
            readingLevel = collection.readingLevel,
            primaryLanguage = collection.primaryLanguage,
            secondaryLanguages = collection.secondaryLanguages?.value?.map { it.name } as List<String>?,
            primaryGenre = collection.primaryGenre,
            secondaryGenres = collection.secondaryGenres?.value?.map { it.name } as List<String>?,
        )

    fun toDomain(entity: CollectionEntity): Collection =
        Collection(
            id = CollectionId(entity.id),
            name = CollectionName(entity.name),
            readingLevel = entity.readingLevel,
            primaryLanguage = entity.primaryLanguage,
            secondaryLanguages =
                entity.secondaryLanguages
                    ?.map {
                        Language.valueOf(
                            it,
                        )
                    }?.let { SecondaryLanguages(it) },
            primaryGenre = entity.primaryGenre,
            secondaryGenres = entity.secondaryGenres?.map { Genre.valueOf(it) }?.let { SecondaryGenres(it) },
        )

    fun toDomainSummary(entity: CollectionEntity): CollectionSummary =
        CollectionSummary(
            id = CollectionId(entity.id),
            name = CollectionName(entity.name),
            readingLevel = entity.readingLevel,
            primaryLanguage = entity.primaryLanguage,
            primaryGenre = entity.primaryGenre,
        )
}
