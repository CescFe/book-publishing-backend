package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.ListCollectionsPaginatedUseCase
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ListCollectionsPaginatedApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAuthorsPaginated200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListCollectionsPaginated200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListCollectionsPaginated200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.PaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListCollectionsPaginated")
class ListCollectionsPaginatedController(
    private val listCollectionsPaginatedUseCase: ListCollectionsPaginatedUseCase,
) : ListCollectionsPaginatedApi {
    override fun listCollectionsPaginated(
        limit: Int,
        page: Int
    ): ResponseEntity<ListCollectionsPaginated200ResponseDTO> {
        val query = ListCollectionsPaginatedUseCase.Query(page, limit)
        val result = listCollectionsPaginatedUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<CollectionSummary>): ListCollectionsPaginated200ResponseDTO =
        ListCollectionsPaginated200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAuthorsPaginated200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(domain: CollectionSummary): ListCollectionsPaginated200ResponseDataInnerDTO =
        ListCollectionsPaginated200ResponseDataInnerDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    ListCollectionsPaginated200ResponseDataInnerDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    ListCollectionsPaginated200ResponseDataInnerDTO.PrimaryLanguage.valueOf(it.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    ListCollectionsPaginated200ResponseDataInnerDTO.PrimaryGenre.valueOf(it.name)
                },
        )
}
