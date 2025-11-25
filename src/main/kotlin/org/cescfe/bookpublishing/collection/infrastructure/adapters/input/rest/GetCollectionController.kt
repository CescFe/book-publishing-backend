package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.GetCollectionUseCase
import org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper.CollectionRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.GetCollectionByIdApi
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "GetCollectionById")
class GetCollectionController(
    private val getCollectionUseCase: GetCollectionUseCase,
    private val mapper: CollectionRestMapper,
) : GetCollectionByIdApi {
    override fun getCollectionByID(id: UUID): ResponseEntity<CreateCollection201ResponseDTO> {
        val query = GetCollectionUseCase.Query(collectionId = id.toString())
        val collection = getCollectionUseCase.execute(query)
        val responseDto = mapper.toDto(collection)
        return ResponseEntity.ok(responseDto)
    }
}
