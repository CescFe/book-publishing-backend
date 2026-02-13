package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.UpdateBookByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseDataInnerCollectionDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateBook200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateBookRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "UpdateBookById")
class UpdateBookController(
    private val updateBookUseCase: UpdateBookUseCase,
) : UpdateBookByIdApi {
    override fun updateBook(
        id: UUID,
        updateBookRequestDTO: UpdateBookRequestDTO,
    ): ResponseEntity<UpdateBook200ResponseDTO> {
        val command = mapDtoToCommand(updateBookRequestDTO)
        val updatedBook = updateBookUseCase.execute(id.toString(), command)
        val responseDto = toDto(updatedBook)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapDtoToCommand(dto: UpdateBookRequestDTO): UpdateBookUseCase.Command =
        UpdateBookUseCase.Command(
            title = dto.title,
            authorId = dto.authorId,
            collectionId = dto.collectionId,
            readingLevel = dto.readingLevel?.let { ReadingLevel.valueOf(it.name) },
            primaryLanguage = dto.primaryLanguage?.let { Language.valueOf(it.name) },
            secondaryLanguages = dto.secondaryLanguages?.map { Language.valueOf(it.name) },
            primaryGenre = dto.primaryGenre?.let { Genre.valueOf(it.name) },
            secondaryGenres = dto.secondaryGenres?.map { Genre.valueOf(it.name) },
            basePrice = dto.basePrice,
            vatRate = dto.vatRate,
            isbn = dto.isbn,
            publicationDate = dto.publicationDate,
            pageCount = dto.pageCount,
            coverImagePath = dto.coverImageUrl,
            description = dto.description,
            status = dto.status?.let { Status.valueOf(it.name) },
        )

    private fun toDto(domain: Book): UpdateBook200ResponseDTO =
        UpdateBook200ResponseDTO(
            id = domain.id.value,
            title = domain.title.value,
            author =
                GetBooks200ResponseDataInnerAuthorDTO(
                    id = domain.authorId.value,
                    name = domain.authorName!!,
                ),
            collection =
                GetBooks200ResponseDataInnerCollectionDTO(
                    id = domain.collectionId.value,
                    name = domain.collectionName!!,
                ),
            readingLevel =
                domain.readingLevel?.let {
                    UpdateBook200ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    UpdateBook200ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    UpdateBook200ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    UpdateBook200ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    UpdateBook200ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            basePrice = domain.basePrice.value,
            vatRate = domain.vatRate?.value,
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImagePath = domain.coverImagePath?.value,
            description = domain.description?.value,
            status = domain.status?.let { UpdateBook200ResponseDTO.Status.valueOf(it.name) },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
