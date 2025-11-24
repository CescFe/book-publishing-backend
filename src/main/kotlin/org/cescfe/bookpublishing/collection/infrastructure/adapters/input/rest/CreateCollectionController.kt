package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.CreateCollectionUseCase
import org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper.CollectionRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateCollectionApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollectionRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

@RestController
@Tag(name = "CreateCollection")
class CreateCollectionController(
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val mapper: CollectionRestMapper,
) : CreateCollectionApi {
    override fun createCollection(
        createCollectionRequestDTO: CreateCollectionRequestDTO,
    ): ResponseEntity<CreateCollection201ResponseDTO> {
        val command = mapDtoToCommand(createCollectionRequestDTO)
        val createdCollection = createCollectionUseCase.execute(command)
        val responseDto = mapper.toDto(createdCollection)
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
            readingLevel = ReadingLevel.valueOf(dto.readingLevel.toString()),
            primaryLanguage = Language.valueOf(dto.primaryLanguage.toString()),
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.toString()) },
            primaryGenre = Genre.valueOf(dto.primaryGenre.toString()),
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.toString()) },
        )
}
