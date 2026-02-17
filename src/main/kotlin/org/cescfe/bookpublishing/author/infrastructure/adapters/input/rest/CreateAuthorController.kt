package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.CreateAuthorApi
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.CreateAuthor201ResponseDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "CreateAuthor")
class CreateAuthorController(
    private val createAuthorUseCase: CreateAuthorUseCase,
) : CreateAuthorApi {
    override fun createAuthor(
        createAuthorRequestDTO: CreateAuthorRequestDTO,
    ): ResponseEntity<CreateAuthor201ResponseDTO> {
        val inputValues = mapDtoToInputValues(createAuthorRequestDTO)
        val createdAuthor = createAuthorUseCase.execute(inputValues)
        val responseDto = toDto(createdAuthor)
        val uri = buildResourceUri(createdAuthor.id.value)
        return ResponseEntity.created(uri).body(responseDto)
    }

    private fun buildResourceUri(authorId: UUID): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authorId)
            .toUri()

    private fun mapDtoToInputValues(dto: CreateAuthorRequestDTO): CreateAuthorUseCase.Command =
        CreateAuthorUseCase.Command(
            fullName = dto.fullName,
            pseudonym = dto.pseudonym,
            biography = dto.biography,
            email = dto.email,
            website = dto.website?.toString(),
        )

    private fun toDto(author: Author): CreateAuthor201ResponseDTO =
        CreateAuthor201ResponseDTO(
            id = author.id.value,
            fullName = author.fullName.value,
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value?.let { URI(it) },
            createdAt = author.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = author.audit?.createdBy,
            updatedAt = author.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = author.audit?.updatedBy,
        )
}
