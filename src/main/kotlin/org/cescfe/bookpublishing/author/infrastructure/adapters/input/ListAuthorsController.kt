package org.cescfe.bookpublishing.author.infrastructure.adapters.input

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllAuthorsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllAuthors")
class ListAuthorsController : GetAllAuthorsApi {
    override fun getAuthors(
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetAuthors200ResponseDTO> =
        ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            GetAuthors200ResponseDTO(
                data = emptyList(),
                meta =
                    GetAuthors200ResponseAllOfMetaDTO(
                        total = 0,
                        page = page,
                        limit = limit,
                        totalPages = 0,
                    ),
            ),
        )
}
