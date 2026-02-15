package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.ListAllBooksUseCase
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ListAllBooksApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllAuthors200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllBooks200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllBooks200ResponseDataInnerDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListBooksPaginated200ResponseDataInnerCollectionDTO
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListAllBooks")
class ListAllBooksController(
    private val listAllBooksUseCase: ListAllBooksUseCase,
) : ListAllBooksApi {
    override fun listAllBooks(): ResponseEntity<ListAllBooks200ResponseDTO> {
        val result = listAllBooksUseCase.execute()
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: NonPaginatedResult<BookSummary>): ListAllBooks200ResponseDTO =
        ListAllBooks200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAllAuthors200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                ),
        )

    private fun toDto(domain: BookSummary): ListAllBooks200ResponseDataInnerDTO =
        ListAllBooks200ResponseDataInnerDTO(
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
                    ListAllBooks200ResponseDataInnerDTO.Status.valueOf(it.name)
                },
        )
}
