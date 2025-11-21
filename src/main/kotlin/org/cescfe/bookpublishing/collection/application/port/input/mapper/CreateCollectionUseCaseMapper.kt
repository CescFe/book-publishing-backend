package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CollectionDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
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

    fun toDto(domain: Collection): CollectionDTO =
        CollectionDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel = domain.readingLevel?.let { CollectionDTO.ReadingLevel.valueOf(it.name) },
            primaryLanguage = domain.primaryLanguage?.let { CollectionDTO.PrimaryLanguage.valueOf(it.name) },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map {
                    CollectionDTO.SecondaryLanguages.valueOf(
                        it.name,
                    )
                },
            primaryGenre = domain.primaryGenre?.let { CollectionDTO.PrimaryGenre.valueOf(it.name) },
            secondaryGenres =
                domain.secondaryGenres?.value?.map {
                    CollectionDTO.SecondaryGenres.valueOf(
                        it.name,
                    )
                },
            createdAt = null,
            createdBy = null,
            updatedAt = null,
            updatedBy = null,
        )

    fun toInputValues(dto: CollectionDTO): CreateCollectionUseCase.InputValues =
        CreateCollectionUseCase.InputValues(
            name = dto.name,
            readingLevel = dto.readingLevel?.let { ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.name) },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.name) },
        )
}
