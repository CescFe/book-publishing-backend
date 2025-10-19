package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.UpdateAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateAuthorRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "UpdateAuthorById")
class UpdateAuthorController(
    private val updateAuthorUseCase: UpdateAuthorUseCase,
    private val mapper: AuthorRestMapper,
) : UpdateAuthorByIdApi {
    override fun updateAuthor(
        id: UUID,
        updateAuthorRequestDTO: UpdateAuthorRequestDTO,
    ): ResponseEntity<CreateAuthorRequestDTO> {
        val inputValues = mapPathAndDtoToInputValues(id, updateAuthorRequestDTO)
        val updatedAuthor = updateAuthorUseCase.execute(inputValues)
        val responseDto = mapper.toDto(updatedAuthor)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapPathAndDtoToInputValues(
        authorId: UUID,
        dto: UpdateAuthorRequestDTO,
    ): UpdateAuthorUseCase.InputValues =
        UpdateAuthorUseCase.InputValues(
            authorId = authorId.toString(),
            fullName = dto.fullName,
            roles = dto.roles.map { it.value }.toSet(),
            pseudonym = dto.pseudonym,
            biography = dto.biography,
            email = dto.email,
            website = dto.website?.toString(),
            version = dto.version,
        )
}
