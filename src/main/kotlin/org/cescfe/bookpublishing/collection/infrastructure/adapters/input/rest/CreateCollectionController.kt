package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.CreateCollectionApi
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.CreateCollectionRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "CreateCollection")
class CreateCollectionController(
    private val createCollectionUseCase: CreateCollectionUseCase,
) : CreateCollectionApi {
    override fun createCollection(
        createCollectionRequestDTO: CreateCollectionRequestDTO,
    ): ResponseEntity<CreateCollection201ResponseDTO> {
        val command = mapDtoToCommand(createCollectionRequestDTO)
        val createdCollection = createCollectionUseCase.execute(command)
        val responseDto = toDto(createdCollection)
        val uri = buildResourceUri(createdCollection.id.value)
        return ResponseEntity.created(uri).body(responseDto)
    }

    private fun buildResourceUri(collectionId: UUID): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(collectionId)
            .toUri()

    private fun mapDtoToCommand(dto: CreateCollectionRequestDTO): CreateCollectionUseCase.Command =
        CreateCollectionUseCase.Command(
            name = dto.name,
            readingLevel = dto.readingLevel?.let { ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.name) },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.name) },
        )

    private fun toDto(domain: Collection): CreateCollection201ResponseDTO =
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
