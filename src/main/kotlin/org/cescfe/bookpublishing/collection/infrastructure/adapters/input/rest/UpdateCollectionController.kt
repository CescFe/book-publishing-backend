package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.UpdateCollectionByIdApi
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.UpdateCollection200ResponseDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.UpdateCollectionRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "UpdateCollectionById")
class UpdateCollectionController(
    private val updateCollectionUseCase: UpdateCollectionUseCase,
) : UpdateCollectionByIdApi {
    override fun updateCollection(
        id: UUID,
        updateCollectionRequestDTO: UpdateCollectionRequestDTO,
    ): ResponseEntity<UpdateCollection200ResponseDTO> {
        val command = mapDtoToCommand(updateCollectionRequestDTO)
        val updatedCollection = updateCollectionUseCase.execute(id.toString(), command)
        val responseDto = toDto(updatedCollection)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapDtoToCommand(dto: UpdateCollectionRequestDTO): UpdateCollectionUseCase.Command =
        UpdateCollectionUseCase.Command(
            name = dto.name,
            readingLevel = dto.readingLevel?.let { ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.name) },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.name) },
        )

    private fun toDto(domain: Collection): UpdateCollection200ResponseDTO =
        UpdateCollection200ResponseDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    UpdateCollection200ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    UpdateCollection200ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    UpdateCollection200ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    UpdateCollection200ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    UpdateCollection200ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
