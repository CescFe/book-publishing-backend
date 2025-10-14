package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.Author
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllAuthorsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfDataInnerDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.OffsetDateTime

@RestController
@Tag(name = "GetAllAuthors")
class ListAuthorsController(
    private val listAuthorsUseCase: ListAuthorsUseCase,
) : GetAllAuthorsApi {
    override fun getAuthors(
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetAuthors200ResponseDTO> {
        val inputValues = mapParametersToInputValues(page, limit, search)
        val result = listAuthorsUseCase.execute(inputValues)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapParametersToInputValues(
        page: Int,
        limit: Int,
        search: String?,
    ): ListAuthorsUseCase.InputValues =
        ListAuthorsUseCase.InputValues(
            page = page,
            limit = limit,
            search = search,
        )

    private fun mapResultToDto(result: PaginatedResult<Author>): GetAuthors200ResponseDTO =
        GetAuthors200ResponseDTO(
            data = result.data.map { mapAuthorToDto(it) },
            meta =
                GetAuthors200ResponseAllOfMetaDTO(
                    total = result.meta.total.toInt(),
                    page = result.meta.page,
                    limit = result.meta.limit,
                    totalPages = result.meta.totalPages,
                ),
        )

    private fun mapAuthorToDto(author: Author): GetAuthors200ResponseAllOfDataInnerDTO =
        GetAuthors200ResponseAllOfDataInnerDTO(
            id = author.id.value,
            fullName = author.fullName.value,
            roles =
                author.roles.map {
                    GetAuthors200ResponseAllOfDataInnerDTO.Roles.forValue(it.value)
                },
            pseudonym = author.pseudonym?.value,
            biography = author.biography?.value,
            email = author.email?.value,
            website = author.website?.value?.let { URI(it) },
            version = 1L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now(),
        )
}
