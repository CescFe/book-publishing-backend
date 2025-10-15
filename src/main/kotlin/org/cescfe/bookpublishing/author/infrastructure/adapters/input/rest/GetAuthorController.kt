package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAuthorByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "GetAuthorById")
class GetAuthorController(
    private val getAuthorUseCase: GetAuthorUseCase,
    private val mapper: AuthorMapper,
) : GetAuthorByIdApi {
    override fun getAuthorByID(id: UUID): ResponseEntity<GetAuthors200ResponseAllOfDataInnerDTO> {
        val inputValues = mapPathToInputValues(id)
        val author = getAuthorUseCase.execute(inputValues)
        val responseDto = mapper.toDto(author)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapPathToInputValues(authorId: UUID): GetAuthorUseCase.InputValues =
        GetAuthorUseCase.InputValues(authorId = authorId.toString())
}
