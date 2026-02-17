package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.DeleteAuthorByIdApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "DeleteAuthorById")
class DeleteAuthorController(
    private val deleteAuthorUseCase: DeleteAuthorUseCase,
) : DeleteAuthorByIdApi {
    override fun deleteAuthor(id: UUID): ResponseEntity<Unit> {
        val command = DeleteAuthorUseCase.Command(authorId = id.toString())
        deleteAuthorUseCase.execute(command)
        return ResponseEntity.noContent().build()
    }
}
