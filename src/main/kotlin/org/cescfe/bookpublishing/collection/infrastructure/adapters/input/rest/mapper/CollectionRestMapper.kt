package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper

import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class CollectionRestMapper {
    fun toDto(domain: Collection): CreateCollection201ResponseDTO =
        CreateCollection201ResponseDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    CreateCollection201ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    CreateCollection201ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    CreateCollection201ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    CreateCollection201ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    CreateCollection201ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
