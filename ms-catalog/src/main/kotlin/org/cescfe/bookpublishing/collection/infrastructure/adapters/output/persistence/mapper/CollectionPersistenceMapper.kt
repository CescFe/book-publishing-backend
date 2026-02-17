package org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.mapper

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.entity.CollectionEntity
import org.cescfe.bookpublishing.collection.infrastructure.adapters.output.persistence.projection.CollectionSummaryProjection
import org.cescfe.bookpublishing.shared.domain.model.Metadata
import org.springframework.stereotype.Component

@Component
class CollectionPersistenceMapper {
    fun fromDomain(collection: Collection): CollectionEntity =
        CollectionEntity(
            id = collection.id.value,
            name = collection.name.value,
            readingLevel = collection.readingLevel,
            primaryLanguage = collection.primaryLanguage,
            secondaryLanguages = collection.secondaryLanguages?.value,
            primaryGenre = collection.primaryGenre,
            secondaryGenres = collection.secondaryGenres?.value,
        )

    fun toDomain(entity: CollectionEntity): Collection =
        Collection(
            id = CollectionId(entity.id),
            name = CollectionName(entity.name),
            readingLevel = entity.readingLevel,
            primaryLanguage = entity.primaryLanguage,
            secondaryLanguages = entity.secondaryLanguages?.let(::SecondaryLanguages),
            primaryGenre = entity.primaryGenre,
            secondaryGenres = entity.secondaryGenres?.let(::SecondaryGenres),
            audit =
                Metadata(
                    createdAt = entity.createdAt,
                    createdBy = entity.createdBy,
                    updatedAt = entity.updatedAt,
                    updatedBy = entity.updatedBy,
                ),
        )

    fun toDomainSummary(projection: CollectionSummaryProjection): CollectionSummary =
        CollectionSummary(
            id = CollectionId(projection.id),
            name = CollectionName(projection.name),
            readingLevel = projection.readingLevel,
            primaryLanguage = projection.primaryLanguage,
            primaryGenre = projection.primaryGenre,
        )
}
