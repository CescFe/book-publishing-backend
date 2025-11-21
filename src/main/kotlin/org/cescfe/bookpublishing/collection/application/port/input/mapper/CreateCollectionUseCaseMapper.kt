package org.cescfe.bookpublishing.collection.application.port.input.mapper

import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.collection.domain.model.CollectionId
import org.cescfe.bookpublishing.collection.domain.model.CollectionName
import org.cescfe.bookpublishing.collection.domain.model.SecondaryGenres
import org.cescfe.bookpublishing.collection.domain.model.SecondaryLanguages
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CollectionDTO
import org.springframework.stereotype.Component
import java.time.ZoneOffset

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
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
