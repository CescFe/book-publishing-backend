package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.exception

import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class TestGlobalExceptionHandler {
    @ExceptionHandler(AuthorDomainException::class)
    fun handleAuthorDomainException(
        exception: AuthorDomainException,
        request: WebRequest,
    ): ResponseEntity<Map<String, Any?>> = mapBySubtype(exception.subType, exception.message ?: "Unknown error")

    @ExceptionHandler(BookDomainException::class)
    fun handleBookDomainException(
        exception: BookDomainException,
        request: WebRequest,
    ): ResponseEntity<Map<String, Any?>> = mapBySubtype(exception.subType, exception.message ?: "Unknown error")

    @ExceptionHandler(CollectionDomainException::class)
    fun handleCollectionDomainException(
        exception: CollectionDomainException,
        request: WebRequest,
    ): ResponseEntity<Map<String, Any?>> = mapBySubtype(exception.subType, exception.message ?: "Unknown error")

    private fun mapBySubtype(
        subType: String,
        message: String,
    ): ResponseEntity<Map<String, Any?>> {
        val status = if (subType.endsWith("_NOT_FOUND")) HttpStatus.NOT_FOUND else HttpStatus.BAD_REQUEST
        val body =
            mapOf(
                "status" to status.value(),
                "error" to status.reasonPhrase,
                "message" to message,
                "code" to subType,
            )
        return ResponseEntity.status(status).body(body)
    }
}
