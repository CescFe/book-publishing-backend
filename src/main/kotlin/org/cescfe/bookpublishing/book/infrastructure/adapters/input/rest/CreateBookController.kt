package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.CreateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper.BookRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateBookApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBookRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

@RestController
@Tag(name = "CreateBook")
class CreateBookController(
    private val createBookUseCase: CreateBookUseCase,
    private val mapper: BookRestMapper,
) : CreateBookApi {
    override fun createBook(createBookRequestDTO: CreateBookRequestDTO): ResponseEntity<CreateBook201ResponseDTO> {
        val command = mapDtoToCommand(createBookRequestDTO)
        val createdBook = createBookUseCase.execute(command)
        val responseDto = mapper.toDto(createdBook)
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
}
