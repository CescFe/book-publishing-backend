package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.UpdateAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateAuthor200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateAuthorRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "UpdateAuthorById")
class UpdateAuthorController(
    private val updateAuthorUseCase: UpdateAuthorUseCase,
) : UpdateAuthorByIdApi {
    override fun updateAuthor(
        id: UUID,
        updateAuthorRequestDTO: UpdateAuthorRequestDTO,
    ): ResponseEntity<UpdateAuthor200ResponseDTO> {
        val command = mapDtoToCommand(updateAuthorRequestDTO)
        val updatedAuthor = updateAuthorUseCase.execute(id.toString(), command)
        val responseDto = toDto(updatedAuthor)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapDtoToCommand(dto: UpdateAuthorRequestDTO): UpdateAuthorUseCase.Command =
        UpdateAuthorUseCase.Command(
            fullName = dto.fullName,
            pseudonym = dto.pseudonym,
            biography = dto.biography,
            email = dto.email,
            website = dto.website?.toString(),
        )

    private fun toDto(author: Author): UpdateAuthor200ResponseDTO =
        UpdateAuthor200ResponseDTO(
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
