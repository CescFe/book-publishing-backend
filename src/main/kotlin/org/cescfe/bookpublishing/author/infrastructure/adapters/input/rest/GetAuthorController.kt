package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthorByID200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "GetAuthorById")
class GetAuthorController(
    private val getAuthorUseCase: GetAuthorUseCase,
) : GetAuthorByIdApi {
    override fun getAuthorByID(id: UUID): ResponseEntity<GetAuthorByID200ResponseDTO> {
        val query = GetAuthorUseCase.Query(authorId = id.toString())
        val author = getAuthorUseCase.execute(query)
        val responseDto = toDto(author)
        return ResponseEntity.ok(responseDto)
    }

    private fun toDto(author: Author): GetAuthorByID200ResponseDTO =
        GetAuthorByID200ResponseDTO(
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
