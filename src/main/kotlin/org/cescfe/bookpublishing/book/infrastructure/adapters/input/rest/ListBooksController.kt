package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.ListBooksUseCase
import org.cescfe.bookpublishing.book.domain.model.BookSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllBooksApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDataInnerCollectionDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllBooks")
class ListBooksController(
    private val listBooksUseCase: ListBooksUseCase,
) : GetAllBooksApi {
    override fun getBooks(): ResponseEntity<GetBooks200ResponseDTO> {
        val result = listBooksUseCase.execute()
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: NonPaginatedResult<BookSummary>): GetBooks200ResponseDTO =
        GetBooks200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                ),
        )

    private fun toDto(domain: BookSummary): GetBooks200ResponseDataInnerDTO =
        GetBooks200ResponseDataInnerDTO(
            id = domain.id.value,
            title = domain.title.value,
            author =
                GetBooks200ResponseDataInnerAuthorDTO(
                    id = domain.authorId.value,
                    name = domain.authorName,
                ),
            collection =
                GetBooks200ResponseDataInnerCollectionDTO(
                    id = domain.collectionId.value,
                    name = domain.collectionName,
                ),
            basePrice = domain.basePrice.value,
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            status =
                domain.status?.let {
                    GetBooks200ResponseDataInnerDTO.Status.valueOf(it.name)
                },
        )
}
