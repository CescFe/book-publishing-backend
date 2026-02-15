package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.ListBooksPaginatedUseCase
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ListBooksPaginatedApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDataInnerCollectionDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListBooksPaginated")
class ListBooksPaginatedController(
    private val listBooksPaginatedUseCase: ListBooksPaginatedUseCase,
) : ListBooksPaginatedApi {
    override fun listBooksPaginated(
        limit: Int,
        page: Int,
    ): ResponseEntity<ListBooksPaginated200ResponseDTO> {
        val query = ListBooksPaginatedUseCase.Query(page, limit)
        val result = listBooksPaginatedUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<BookSummary>): ListBooksPaginated200ResponseDTO =
        ListBooksPaginated200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAuthorsPaginated200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(domain: BookSummary): ListBooksPaginated200ResponseDataInnerDTO =
        ListBooksPaginated200ResponseDataInnerDTO(
            id = domain.id.value,
            title = domain.title.value,
            author =
                ListBooksPaginated200ResponseDataInnerAuthorDTO(
                    id = domain.authorId.value,
                    name = domain.authorName,
                ),
            collection =
                ListBooksPaginated200ResponseDataInnerCollectionDTO(
                    id = domain.collectionId.value,
                    name = domain.collectionName,
                ),
            basePrice = domain.basePrice.value,
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            status =
                domain.status?.let {
                    ListBooksPaginated200ResponseDataInnerDTO.Status.valueOf(it.name)
                },
        )
}
