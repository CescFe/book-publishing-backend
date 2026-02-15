package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.ListAllAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ListAllAuthorsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllAuthors200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllAuthors200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListAllAuthors")
class ListAllAuthorsController(
    private val listAllAuthorsUseCase: ListAllAuthorsUseCase,
) : ListAllAuthorsApi {
    override fun listAllAuthors(): ResponseEntity<ListAllAuthors200ResponseDTO> {
        val result = listAllAuthorsUseCase.execute()
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: NonPaginatedResult<AuthorSummary>): ListAllAuthors200ResponseDTO =
        ListAllAuthors200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAllAuthors200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                ),
        )

    private fun toDto(domain: AuthorSummary): ListAuthorsPaginated200ResponseDataInnerDTO =
        ListAuthorsPaginated200ResponseDataInnerDTO(
            id = domain.id.value,
            fullName = domain.fullName.value,
            pseudonym = domain.pseudonym?.value,
            email = domain.email?.value,
        )
}
