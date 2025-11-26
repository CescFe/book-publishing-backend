package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.collection.application.port.input.ListCollectionsUseCase
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllCollectionsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseAllOfMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetCollections200ResponseAllOfDataInnerDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetCollections200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllCollections")
class ListCollectionsController(
    private val listCollectionsUseCase: ListCollectionsUseCase,
) : GetAllCollectionsApi {
    override fun getCollections(
        page: Int,
        limit: Int,
        search: String?,
    ): ResponseEntity<GetCollections200ResponseDTO> {
        val query = ListCollectionsUseCase.Query(page, limit)
        val result = listCollectionsUseCase.execute(query)
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: PaginatedResult<CollectionSummary>): GetCollections200ResponseDTO =
        GetCollections200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseAllOfMetaDTO(
                    total = result.metadata.total.toInt(),
                    page = result.metadata.page,
                    limit = result.metadata.limit,
                    totalPages = result.metadata.totalPages,
                ),
        )

    private fun toDto(domain: CollectionSummary): GetCollections200ResponseAllOfDataInnerDTO =
        GetCollections200ResponseAllOfDataInnerDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    GetCollections200ResponseAllOfDataInnerDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    GetCollections200ResponseAllOfDataInnerDTO.PrimaryLanguage.valueOf(it.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    GetCollections200ResponseAllOfDataInnerDTO.PrimaryGenre.valueOf(it.name)
                },
        )
}
