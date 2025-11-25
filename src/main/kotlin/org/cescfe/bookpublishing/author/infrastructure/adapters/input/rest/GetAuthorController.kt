package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthor201ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "GetAuthorById")
class GetAuthorController(
    private val getAuthorUseCase: GetAuthorUseCase,
    private val mapper: AuthorRestMapper,
) : GetAuthorByIdApi {
    override fun getAuthorByID(id: UUID): ResponseEntity<CreateAuthor201ResponseDTO> {
        val query = GetAuthorUseCase.Query(authorId = id.toString())
        val author = getAuthorUseCase.execute(query)
        val responseDto = mapper.toDto(author)
        return ResponseEntity.ok(responseDto)
    }
}
