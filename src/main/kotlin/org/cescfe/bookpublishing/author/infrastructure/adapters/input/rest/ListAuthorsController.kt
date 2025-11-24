package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllAuthorsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllAuthors")
class ListAuthorsController(
    private val listAuthorsUseCase: ListAuthorsUseCase,
) : GetAllAuthorsApi {
    override fun getAuthors(
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetAuthors200ResponseDTO> {
        val inputValues = mapParametersToInputValues(page, limit)
        val result = listAuthorsUseCase.execute(inputValues)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapParametersToInputValues(
        page: Int,
        limit: Int,
    ): ListAuthorsUseCase.InputValues =
        ListAuthorsUseCase.InputValues(
            page = page,
            limit = limit,
        )

    private fun mapResultToDto(result: PaginatedResult<AuthorSummary>): GetAuthors200ResponseDTO =
        GetAuthors200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseAllOfMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(authorSummary: AuthorSummary): GetAuthors200ResponseAllOfDataInnerDTO =
        GetAuthors200ResponseAllOfDataInnerDTO(
            id = authorSummary.id.value,
            fullName = authorSummary.fullName.value,
            pseudonym = authorSummary.pseudonym?.value,
            email = authorSummary.email?.value,
        )
}
