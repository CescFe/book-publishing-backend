package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.domain.model.enum.Status
import org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper.BookRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.UpdateBookByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBookRequestDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "UpdateBookById")
class UpdateBookController(
    private val updateBookUseCase: UpdateBookUseCase,
    private val mapper: BookRestMapper,
) : UpdateBookByIdApi {
    override fun updateBook(
        id: UUID,
        createBookRequestDTO: CreateBookRequestDTO,
    ): ResponseEntity<CreateBook201ResponseDTO> {
        val command = mapDtoToCommand(createBookRequestDTO)
        val updatedBook = updateBookUseCase.execute(id.toString(), command)
        val responseDto = mapper.toDto(updatedBook)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapDtoToCommand(dto: CreateBookRequestDTO): UpdateBookUseCase.Command =
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
}
