package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID

@RestController
@Tag(name = "GetAuthorById")
class GetAuthorController(
    private val getAuthorUseCase: GetAuthorUseCase,
) : GetAuthorByIdApi {
    override fun getAuthorByID(id: UUID): ResponseEntity<GetAuthors200ResponseAllOfDataInnerDTO> {
        val inputValues = mapPathToInputValues(id)
        val author = getAuthorUseCase.execute(inputValues)
        val responseDto = mapAuthorToDto(author)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapPathToInputValues(authorId: UUID): GetAuthorUseCase.InputValues =
        GetAuthorUseCase.InputValues(authorId = authorId.toString())

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
            website = author.website?.value?.let { URI(it) },
            version = 1L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now(),
        )
}
