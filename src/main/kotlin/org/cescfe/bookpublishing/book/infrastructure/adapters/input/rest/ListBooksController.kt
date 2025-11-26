package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.book.application.port.input.ListBooksUseCase
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllBooksApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseAllOfDataInnerDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllBooks")
class ListBooksController(
    private val listBooksUseCase: ListBooksUseCase,
) : GetAllBooksApi {
    override fun getBooks(
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetBooks200ResponseDTO> {
        val query = ListBooksUseCase.Query(page, limit)
        val result = listBooksUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<BookSummary>): GetBooks200ResponseDTO =
        GetBooks200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseAllOfMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(domain: BookSummary): GetBooks200ResponseAllOfDataInnerDTO =
        GetBooks200ResponseAllOfDataInnerDTO(
            id = domain.id.value,
            title = domain.title.value,
            authorId = domain.authorId.value,
            collectionId = domain.collectionId.value,
            basePrice = domain.basePrice.value,
            finalPrice = null,
            isbn = domain.isbn?.value,
            status =
                domain.status?.let {
                    GetBooks200ResponseAllOfDataInnerDTO.Status.valueOf(it.name)
                },
        )
}
