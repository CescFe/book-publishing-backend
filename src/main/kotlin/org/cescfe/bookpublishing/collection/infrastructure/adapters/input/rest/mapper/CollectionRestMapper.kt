package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CollectionDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollectionRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.stereotype.Component

@Component
class CollectionRestMapper {
    fun toDomain(dto: CollectionDTO): Collection =
        Collection(
            id = dto.id?.let { CollectionId(it) } ?: CollectionId.generate(),
            name = CollectionName(dto.name),
            readingLevel = dto.readingLevel?.value?.let { ReadingLevel.valueOf(it) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages =
                SecondaryLanguages(
                    dto.secondaryLanguages?.map {
                        Language.valueOf(it.value)
                    } ?: emptyList(),
                ),
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres =
                SecondaryGenres(
                    dto.secondaryGenres?.map {
                        Genre.valueOf(it.value)
                    } ?: emptyList(),
                ),
        )

    fun toResponse(dto: CollectionDTO): CreateCollectionRequestDTO =
        CreateCollectionRequestDTO(
            id = dto.id,
            name = dto.name,
            readingLevel = dto.readingLevel?.let { CreateCollectionRequestDTO.ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { CreateCollectionRequestDTO.PrimaryLanguage.valueOf(it.name) },
            secondaryLanguages =
                dto.secondaryLanguages?.map {
                    CreateCollectionRequestDTO.SecondaryLanguages.valueOf(it.name)
                } ?: emptyList(),
            primaryGenre = dto.primaryGenre?.let { CreateCollectionRequestDTO.PrimaryGenre.valueOf(it.name) },
            secondaryGenres =
                dto.secondaryGenres?.map {
                    CreateCollectionRequestDTO.SecondaryGenres.valueOf(it.name)
                } ?: emptyList(),
            createdAt = dto.createdAt,
            createdBy = dto.createdBy,
            updatedAt = dto.updatedAt,
            updatedBy = dto.updatedBy,
        )
}
