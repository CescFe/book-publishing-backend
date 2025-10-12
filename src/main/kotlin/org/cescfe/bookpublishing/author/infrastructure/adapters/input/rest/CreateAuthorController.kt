package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateAuthorApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "CreateAuthor")
class CreateAuthorController(
    private val createAuthorUseCase: CreateAuthorUseCase,
) : CreateAuthorApi {
    override fun createAuthor(
        getAuthors200ResponseAllOfDataInnerDTO: GetAuthors200ResponseAllOfDataInnerDTO,
    ): ResponseEntity<GetAuthors200ResponseAllOfDataInnerDTO> {
        val inputValues = mapDtoToInputValues(getAuthors200ResponseAllOfDataInnerDTO)
        val createdAuthor = createAuthorUseCase.execute(inputValues)
        val responseDto = mapAuthorToDto(createdAuthor)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
    }

    private fun mapDtoToInputValues(dto: GetAuthors200ResponseAllOfDataInnerDTO): CreateAuthorUseCase.InputValues =
        CreateAuthorUseCase.InputValues(
            fullName = dto.fullName,
            roles = dto.roles.map { it.value }.toSet(),
            pseudonym = dto.pseudonym,
            biography = dto.biography,
            email = dto.email,
            website = dto.website?.toString(),
        )

    private fun mapAuthorToDto(author: Author): GetAuthors200ResponseAllOfDataInnerDTO =
        GetAuthors200ResponseAllOfDataInnerDTO(
            id = author.id.value,
            fullName = author.fullName.value,
            roles =
                author.roles.map {
                    GetAuthors200ResponseAllOfDataInnerDTO.Roles.forValue(it.value)
                },
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value?.let { java.net.URI(it) },
            version = 1L,
            createdAt = java.time.OffsetDateTime.now(),
            updatedAt = java.time.OffsetDateTime.now(),
        )
}
