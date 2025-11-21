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
import java.time.ZoneOffset

@Component
class CollectionRestMapper {
    fun toDomain(dto: CollectionDTO): Collection =
        Collection(
            id = dto.id?.let { CollectionId(it) } ?: CollectionId.generate(),
            name = CollectionName(dto.name),
            readingLevel = dto.readingLevel?.value?.let { ReadingLevel.valueOf(it) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages =
                dto.secondaryLanguages?.let {
                    SecondaryLanguages(
                        it.map { langEnum ->
                            Language.valueOf(langEnum.value)
                        },
                    )
                },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres =
                dto.secondaryGenres?.let {
                    SecondaryGenres(
                        it.map { genreEnum ->
                            Genre.valueOf(genreEnum.value)
                        },
                    )
                },
        )

    fun toDto(domain: Collection): CreateCollectionRequestDTO =
        CreateCollectionRequestDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    CreateCollectionRequestDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    CreateCollectionRequestDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    CreateCollectionRequestDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    CreateCollectionRequestDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    CreateCollectionRequestDTO.SecondaryGenres.valueOf(genre.name)
                },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
