package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.GetBookUseCase
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetBookByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBookByID200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseOneOfAllOfDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseOneOfAllOfDataInnerCollectionDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "GetBookById")
class GetBookController(
    private val getBookUseCase: GetBookUseCase,
) : GetBookByIdApi {
    override fun getBookByID(id: UUID): ResponseEntity<GetBookByID200ResponseDTO> {
        val query = GetBookUseCase.Query(bookId = id.toString())
        val book = getBookUseCase.execute(query)
        val responseDto = toDto(book)
        return ResponseEntity.ok(responseDto)
    }

    private fun toDto(domain: Book): GetBookByID200ResponseDTO =
        GetBookByID200ResponseDTO(
            id = domain.id.value,
            title = domain.title.value,
            author =
                GetBooks200ResponseOneOfAllOfDataInnerAuthorDTO(
                    id = domain.authorId.value,
                    name = domain.authorName!!,
                ),
            collection =
                GetBooks200ResponseOneOfAllOfDataInnerCollectionDTO(
                    id = domain.collectionId.value,
                    name = domain.collectionName!!,
                ),
            readingLevel =
                domain.readingLevel?.let {
                    GetBookByID200ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    GetBookByID200ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    GetBookByID200ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    GetBookByID200ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    GetBookByID200ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            basePrice = domain.basePrice.value,
            vatRate = domain.vatRate?.value,
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImagePath = domain.coverImagePath?.value,
            description = domain.description?.value,
            status = domain.status?.let { GetBookByID200ResponseDTO.Status.valueOf(it.name) },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
