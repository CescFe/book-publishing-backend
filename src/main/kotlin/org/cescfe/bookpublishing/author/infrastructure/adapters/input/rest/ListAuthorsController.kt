package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllAuthorsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseOneOf1MetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseOneOfAllOfDataInnerDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllAuthors")
class ListAuthorsController(
    private val listAuthorsUseCase: ListAuthorsUseCase,
) : GetAllAuthorsApi {
    override fun getAuthors(
        paginate: Boolean,
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetAuthors200ResponseDTO> {
        val query = ListAuthorsUseCase.Query(page, limit)
        val result = listAuthorsUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<AuthorSummary>): GetAuthors200ResponseDTO =
        GetAuthors200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseOneOf1MetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(domain: AuthorSummary): GetAuthors200ResponseOneOfAllOfDataInnerDTO =
        GetAuthors200ResponseOneOfAllOfDataInnerDTO(
            id = domain.id.value,
            fullName = domain.fullName.value,
            pseudonym = domain.pseudonym?.value,
            email = domain.email?.value,
        )
}
