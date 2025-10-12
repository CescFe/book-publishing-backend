package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.CreateAuthorControllerMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.CreateAuthorApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "CreateAuthor")
class CreateAuthorController(
    private val createAuthorUseCase: CreateAuthorUseCase,
    private val mapper: CreateAuthorControllerMapper,
) : CreateAuthorApi {
    override fun createAuthor(
        getAuthors200ResponseAllOfDataInnerDTO: GetAuthors200ResponseAllOfDataInnerDTO,
    ): ResponseEntity<GetAuthors200ResponseAllOfDataInnerDTO> {
        val inputValues = mapper.toInputValues(getAuthors200ResponseAllOfDataInnerDTO)
        val createdAuthor = createAuthorUseCase.execute(inputValues)
        val responseDto = mapper.toDto(createdAuthor)

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
    }
}
