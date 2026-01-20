package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper.CollectionRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.UpdateCollectionByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollectionRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "UpdateCollectionById")
class UpdateCollectionController(
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    private val mapper: CollectionRestMapper,
) : UpdateCollectionByIdApi {
    override fun updateCollection(
        id: UUID,
        createCollectionRequestDTO: CreateCollectionRequestDTO,
    ): ResponseEntity<CreateCollection201ResponseDTO> {
        val command = mapDtoToCommand(createCollectionRequestDTO)
        val updatedCollection = updateCollectionUseCase.execute(id.toString(), command)
        val responseDto = mapper.toDto(updatedCollection)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapDtoToCommand(dto: CreateCollectionRequestDTO): UpdateCollectionUseCase.Command =
        UpdateCollectionUseCase.Command(
            name = dto.name,
            readingLevel = dto.readingLevel?.let { ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.name) },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.name) },
        )
}
