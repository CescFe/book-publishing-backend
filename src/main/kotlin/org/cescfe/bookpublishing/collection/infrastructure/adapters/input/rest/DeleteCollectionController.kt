package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.collection.application.port.input.DeleteCollectionUseCase
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.DeleteCollectionByIdApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "DeleteCollectionById")
class DeleteCollectionController(
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
) : DeleteCollectionByIdApi {
    override fun deleteCollection(id: UUID): ResponseEntity<Unit> {
        val command = DeleteCollectionUseCase.Command(collectionId = id.toString())
        deleteCollectionUseCase.execute(command)
        return ResponseEntity.noContent().build()
    }
}
