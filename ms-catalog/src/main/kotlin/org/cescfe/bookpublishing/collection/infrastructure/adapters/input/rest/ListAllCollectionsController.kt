package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.ListAllCollectionsUseCase
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ListAllCollectionsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllAuthors200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListAllCollections200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.ListCollectionsPaginated200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "ListAllCollections")
class ListAllCollectionsController(
    private val listAllCollectionsUseCase: ListAllCollectionsUseCase,
) : ListAllCollectionsApi {
    override fun listAllCollections(): ResponseEntity<ListAllCollections200ResponseDTO> {
        val result = listAllCollectionsUseCase.execute()
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: NonPaginatedResult<CollectionSummary>): ListAllCollections200ResponseDTO =
        ListAllCollections200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                ListAllAuthors200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
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
