package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.GetBookUseCase
import org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper.BookRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetBookByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBook201ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "GetBookById")
class GetBookController(
    private val getBookUseCase: GetBookUseCase,
    private val mapper: BookRestMapper,
) : GetBookByIdApi {
    override fun getBookByID(id: UUID): ResponseEntity<CreateBook201ResponseDTO> {
        val inputValues = mapPathToInputValues(id)
        val book = getBookUseCase.execute(inputValues)
        val responseDto = mapper.toDto(book)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapPathToInputValues(bookId: UUID): GetBookUseCase.Query =
        GetBookUseCase.Query(bookId = bookId.toString())
}
