package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.Book
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateBookApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBookRequestDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseOneOfAllOfDataInnerAuthorDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetBooks200ResponseOneOfAllOfDataInnerCollectionDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "CreateBook")
class CreateBookController(
    private val createBookUseCase: CreateBookUseCase,
) : CreateBookApi {
    override fun createBook(createBookRequestDTO: CreateBookRequestDTO): ResponseEntity<CreateBook201ResponseDTO> {
        val command = mapDtoToCommand(createBookRequestDTO)
        val createdBook = createBookUseCase.execute(command)
        val responseDto = toDto(createdBook)
        val uri = buildResourceUri(createdBook.id.value)
        return ResponseEntity.created(uri).body(responseDto)
    }

    private fun buildResourceUri(bookId: UUID): URI =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(bookId)
            .toUri()

    private fun mapDtoToCommand(dto: CreateBookRequestDTO): CreateBookUseCase.Command =
        CreateBookUseCase.Command(
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
            coverImagePath = dto.coverImageUrl.toString(),
            description = dto.description,
            status = dto.status?.let { Status.valueOf(it.name) },
        )

    private fun toDto(domain: Book): CreateBook201ResponseDTO =
        CreateBook201ResponseDTO(
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
                    CreateBook201ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    CreateBook201ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    CreateBook201ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    CreateBook201ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    CreateBook201ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            basePrice = domain.basePrice.value,
            vatRate = domain.vatRate?.value,
            finalPrice = domain.finalPrice,
            isbn = domain.isbn?.value,
            publicationDate = domain.publicationDate?.value,
            pageCount = domain.pageCount?.value,
            coverImagePath = domain.coverImagePath?.value,
            description = domain.description?.value,
            status = domain.status?.let { CreateBook201ResponseDTO.Status.valueOf(it.name) },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
