package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsPaginatedUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.ListAuthorsPaginatedApi
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseDataInnerDTO
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseMetaDTO
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListAuthorsPaginated")
class ListAuthorsPaginatedController(
    private val listAuthorsUseCase: ListAuthorsPaginatedUseCase,
) : ListAuthorsPaginatedApi {
    override fun listAuthorsPaginated(
        limit: Int,
        page: Int,
    ): ResponseEntity<ListAuthorsPaginated200ResponseDTO> {
        val query = ListAuthorsPaginatedUseCase.Query(page, limit)
        val result = listAuthorsUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<AuthorSummary>): ListAuthorsPaginated200ResponseDTO =
        ListAuthorsPaginated200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAuthorsPaginated200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
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
