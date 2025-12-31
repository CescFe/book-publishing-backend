package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.UpdateBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest.mapper.BookRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateBookRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

class UpdateBookControllerTest {
    private lateinit var updateBookUseCase: UpdateBookUseCase
    private lateinit var mapper: BookRestMapper
    private lateinit var updateBookController: UpdateBookController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Book with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "BOOK_NOT_FOUND"
        private const val ISBN_CONFLICT_MESSAGE = "Book with ISBN '%s' already exists"
        private const val ISBN_CONFLICT_SUBTYPE = "ISBN_ALREADY_EXISTS"
        private const val NON_EXISTENT_BOOK_ID = "223e4567-e89b-12d3-a456-426614174000"
        private const val EXISTING_ISBN = "9780007141326"
    }

    @BeforeEach
    fun setup() {
        updateBookUseCase = mock()
        mapper = mock()
        updateBookController = UpdateBookController(updateBookUseCase, mapper)
    }

    @Test
    fun `should throw BookDomainException when updating non-existent book`() {
        // Given
        val bookId = UUID.fromString(NON_EXISTENT_BOOK_ID)
        val requestDTO =
            CreateBookRequestDTO(
                title = "Updated Book",
                authorId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001"),
                collectionId = UUID.fromString("223e4567-e89b-12d3-a456-426614174002"),
                basePrice = 19.99,
            )

        whenever(
            updateBookUseCase.execute(
                any(),
                any(),
            ),
        ).thenThrow(
            BookDomainException.bookNotFound(NON_EXISTENT_BOOK_ID),
        )

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                updateBookController.updateBook(bookId, requestDTO)
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, NON_EXISTENT_BOOK_ID), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }

    @Test
    fun `should throw BookDomainException when ISBN already exists`() {
        // Given
        val bookId = UUID.fromString(NON_EXISTENT_BOOK_ID)
        val requestDTO =
            CreateBookRequestDTO(
                title = "Updated Book",
                authorId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001"),
                collectionId = UUID.fromString("223e4567-e89b-12d3-a456-426614174002"),
                basePrice = 19.99,
                isbn = EXISTING_ISBN,
            )

        whenever(
            updateBookUseCase.execute(
                any(),
                any(),
            ),
        ).thenThrow(
            BookDomainException.isbnAlreadyExists(EXISTING_ISBN),
        )

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                updateBookController.updateBook(bookId, requestDTO)
            }
        assertEquals(String.format(ISBN_CONFLICT_MESSAGE, EXISTING_ISBN), exception.message)
        assertEquals(ISBN_CONFLICT_SUBTYPE, exception.subType)
    }
}
