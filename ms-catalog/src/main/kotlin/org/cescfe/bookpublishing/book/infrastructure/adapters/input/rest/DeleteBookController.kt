package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import io.swagger.v3.oas.annotations.tags.Tag
import org.cescfe.bookpublishing.book.application.port.input.DeleteBookUseCase
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.DeleteBookByIdApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "DeleteBookById")
class DeleteBookController(
    private val deleteBookUseCase: DeleteBookUseCase,
) : DeleteBookByIdApi {
    override fun deleteBook(id: UUID): ResponseEntity<Unit> {
        val command = DeleteBookUseCase.Command(bookId = id.toString())
        deleteBookUseCase.execute(command)
        return ResponseEntity.noContent().build()
    }
}
