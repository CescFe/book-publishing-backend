package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.exception

import jakarta.validation.ConstraintViolationException
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ApiException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.NotFoundException
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.exception.model.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BookDomainException::class)
    fun handleBookDomainException(
        ex: BookDomainException,
        request: WebRequest,
    ): ResponseEntity<ApiError> {
        val status =
            when (ex.subType) {
                "BOOK_NOT_FOUND" -> HttpStatus.NOT_FOUND
                else -> HttpStatus.BAD_REQUEST
            }

        return buildErrorResponse(
            status = status,
            message = ex.message ?: "Invalid book data",
            code = ex.subType,
            details =
                mapOf(
                    "path" to getPath(request),
                    "exceptionType" to ex.javaClass.simpleName,
                ),
        )
    }

    @ExceptionHandler(AuthorDomainException::class)
    fun handleAuthorDomainException(
        ex: AuthorDomainException,
        request: WebRequest,
    ): ResponseEntity<ApiError> {
        val status =
            when (ex.subType) {
                "AUTHOR_NOT_FOUND" -> HttpStatus.NOT_FOUND
                else -> HttpStatus.BAD_REQUEST
            }

        return buildErrorResponse(
            status = status,
            message = ex.message ?: "Invalid author data",
            code = ex.subType,
            details =
                mapOf(
                    "path" to getPath(request),
                    "exceptionType" to ex.javaClass.simpleName,
                ),
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = "Malformed JSON request: ${ex.message ?: "Invalid request body"}",
            code = "MALFORMED_REQUEST",
            details =
                mapOf(
                    "path" to getPath(request),
                    "exceptionType" to ex.javaClass.simpleName,
                ),
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: WebRequest,
    ): ResponseEntity<ApiError> {
        val violations =
            ex.bindingResult.fieldErrors.associate { error ->
                error.field to (error.defaultMessage ?: "Invalid value")
            }

        return buildErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = "Validation failed",
            code = "VALIDATION_ERROR",
            details =
                mapOf(
                    "path" to getPath(request),
                    "violations" to violations,
                ),
        )
    }

    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(
        ex: NotImplementedError,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.NOT_IMPLEMENTED,
            message = ex.message ?: "Not yet implemented",
            code = "NOT_IMPLEMENTED",
            details = mapOf("path" to getPath(request)),
        )

    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.valueOf(ex.code),
            message = ex.message ?: "An error occurred",
            code = "API_ERROR",
            details = mapOf("path" to getPath(request)),
        )

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        ex: NotFoundException,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.NOT_FOUND,
            message = ex.message ?: "Resource not found",
            code = "NOT_FOUND",
            details = mapOf("path" to getPath(request)),
        )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: WebRequest,
    ): ResponseEntity<ApiError> {
        val violations =
            ex.constraintViolations.associate { violation ->
                violation.propertyPath.toString() to violation.message
            }

        return buildErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = "Validation failed",
            code = "VALIDATION_ERROR",
            details =
                mapOf(
                    "path" to getPath(request),
                    "violations" to violations,
                ),
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = "Invalid parameter type for '${ex.name}': expected ${ex.requiredType?.simpleName}",
            code = "TYPE_MISMATCH",
            details =
                mapOf(
                    "path" to getPath(request),
                    "parameter" to ex.name,
                    "expectedType" to (ex.requiredType?.simpleName ?: "unknown"),
                ),
        )

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(ex: NoHandlerFoundException): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.NOT_FOUND,
            message = "No handler found for ${ex.httpMethod} ${ex.requestURL}",
            code = "NO_HANDLER_FOUND",
            details =
                mapOf(
                    "path" to ex.requestURL,
                    "method" to ex.httpMethod,
                ),
        )

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<ApiError> =
        buildErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = ex.message ?: "An unexpected error occurred",
            code = "INTERNAL_ERROR",
            details =
                mapOf(
                    "path" to getPath(request),
                    "exceptionType" to ex.javaClass.simpleName,
                ),
        )

    private fun buildErrorResponse(
        status: HttpStatus,
        message: String,
        code: String,
        details: Map<String, Any>? = null,
    ): ResponseEntity<ApiError> =
        ResponseEntity
            .status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ApiError(
                    status = status.value(),
                    error = status.reasonPhrase,
                    message = message,
                    code = code,
                    details = details,
                ),
            )

    private fun getPath(request: WebRequest): String = request.getDescription(false).replace("uri=", "")
}
