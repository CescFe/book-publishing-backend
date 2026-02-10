package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.GetCollectionUseCase
import org.cescfe.bookpublishing.collection.domain.model.Collection
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetCollectionByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.GetCollectionByID200ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.util.UUID

@RestController
@Tag(name = "GetCollectionById")
class GetCollectionController(
    private val getCollectionUseCase: GetCollectionUseCase,
) : GetCollectionByIdApi {
    override fun getCollectionByID(id: UUID): ResponseEntity<GetCollectionByID200ResponseDTO> {
        val query = GetCollectionUseCase.Query(collectionId = id.toString())
        val collection = getCollectionUseCase.execute(query)
        val responseDto = toDto(collection)
        return ResponseEntity.ok(responseDto)
    }

    private fun toDto(domain: Collection): GetCollectionByID200ResponseDTO =
        GetCollectionByID200ResponseDTO(
            id = domain.id.value,
            name = domain.name.value,
            readingLevel =
                domain.readingLevel?.let {
                    GetCollectionByID200ResponseDTO.ReadingLevel.valueOf(it.name)
                },
            primaryLanguage =
                domain.primaryLanguage?.let {
                    GetCollectionByID200ResponseDTO.PrimaryLanguage.valueOf(it.name)
                },
            secondaryLanguages =
                domain.secondaryLanguages?.value?.map { lang ->
                    GetCollectionByID200ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                },
            primaryGenre =
                domain.primaryGenre?.let {
                    GetCollectionByID200ResponseDTO.PrimaryGenre.valueOf(it.name)
                },
            secondaryGenres =
                domain.secondaryGenres?.value?.map { genre ->
                    GetCollectionByID200ResponseDTO.SecondaryGenres.valueOf(genre.name)
                },
            createdAt = domain.audit?.createdAt?.atOffset(ZoneOffset.UTC),
            createdBy = domain.audit?.createdBy,
            updatedAt = domain.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
            updatedBy = domain.audit?.updatedBy,
        )
}
