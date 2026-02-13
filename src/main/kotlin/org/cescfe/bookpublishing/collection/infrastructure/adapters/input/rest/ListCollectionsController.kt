package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.ListCollectionsUseCase
import org.cescfe.bookpublishing.collection.domain.model.CollectionSummary
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetAllCollectionsApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetAuthors200ResponseMetaDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetCollections200ResponseDTO
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetCollections200ResponseDataInnerDTO
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "GetAllCollections")
class ListCollectionsController(
    private val listCollectionsUseCase: ListCollectionsUseCase,
) : GetAllCollectionsApi {
    override fun getCollections(): ResponseEntity<GetCollections200ResponseDTO> {
        val result = listCollectionsUseCase.execute()
        val responseDto = mapResultToDto(result)
        return ResponseEntity.ok(responseDto)
    }

    private fun mapResultToDto(result: NonPaginatedResult<CollectionSummary>): GetCollections200ResponseDTO =
        GetCollections200ResponseDTO(
            data = result.data.map { toDto(it) },
            meta =
                GetAuthors200ResponseMetaDTO(
                    total = result.metadata.total.toInt(),
                ),
        )

    private fun toDto(domain: CollectionSummary): GetCollections200ResponseDataInnerDTO =
        GetCollections200ResponseDataInnerDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    GetCollections200ResponseDataInnerDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    GetCollections200ResponseDataInnerDTO.PrimaryLanguage.valueOf(it.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    GetCollections200ResponseDataInnerDTO.PrimaryGenre.valueOf(it.name)
                },
        )
}
