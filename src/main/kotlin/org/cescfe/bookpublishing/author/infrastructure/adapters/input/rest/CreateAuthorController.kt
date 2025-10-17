package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateAuthorApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

@RestController
@Tag(name = "CreateAuthor")
class CreateAuthorController(
    private val createAuthorUseCase: CreateAuthorUseCase,
    private val mapper: AuthorRestMapper,
) : CreateAuthorApi {
    override fun createAuthor(createAuthorRequestDTO: CreateAuthorRequestDTO): ResponseEntity<CreateAuthorRequestDTO> {
        val inputValues = mapDtoToInputValues(createAuthorRequestDTO)
        val createdAuthor = createAuthorUseCase.execute(inputValues)
        val responseDto = mapper.toDto(createdAuthor)
        val uri = buildResourceUri(createdAuthor.id.value)
        return ResponseEntity.created(uri).body(responseDto)
    }

    private fun buildResourceUri(authorId: UUID): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authorId)
            .toUri()

    private fun mapDtoToInputValues(dto: CreateAuthorRequestDTO): CreateAuthorUseCase.InputValues =
        CreateAuthorUseCase.InputValues(
            fullName = dto.fullName,
            roles = dto.roles.map { it.value }.toSet(),
            pseudonym = dto.pseudonym,
            biography = dto.biography,
            email = dto.email,
            website = dto.website?.toString(),
        )
}
