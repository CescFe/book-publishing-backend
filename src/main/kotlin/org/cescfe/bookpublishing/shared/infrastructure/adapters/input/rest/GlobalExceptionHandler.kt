package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.rest

import jakarta.validation.ConstraintViolationException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.ApiException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(
        ex: NotImplementedError,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_IMPLEMENTED.value(),
                error = "Not Implemented",
                message = ex.message ?: "Not yet implemented",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse)
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = ex.code,
                error = HttpStatus.valueOf(ex.code).reasonPhrase,
                message = ex.message ?: "An error occurred",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(ex.code).body(errorResponse)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        ex: NotFoundException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                error = "Not Found",
                message = ex.message ?: "Resource not found",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = ex.constraintViolations.joinToString(", ") { it.message },
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = "Invalid parameter type for '${ex.name}': expected ${ex.requiredType?.simpleName}",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(
        ex: NoHandlerFoundException,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                error = "Not Found",
                message = "No handler found for ${ex.httpMethod} ${ex.requestURL}",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Internal Server Error",
                message = ex.message ?: "An unexpected error occurred",
                path = request.getDescription(false).replace("uri=", ""),
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
