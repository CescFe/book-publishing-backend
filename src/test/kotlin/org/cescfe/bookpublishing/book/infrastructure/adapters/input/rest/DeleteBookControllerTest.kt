package org.cescfe.bookpublishing.book.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.book.application.port.input.DeleteBookUseCase
import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteBookControllerTest {
    private lateinit var deleteBookUseCase: DeleteBookUseCase
    private lateinit var deleteBookController: DeleteBookController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Book with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "BOOK_NOT_FOUND"
        private const val NON_EXISTENT_BOOK_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        deleteBookUseCase = mock()
        deleteBookController = DeleteBookController(deleteBookUseCase)
    }

    @Test
    fun `should throw BookDomainException when deleting non-existent book`() {
        // Given
        whenever(deleteBookUseCase.execute(DeleteBookUseCase.Command(NON_EXISTENT_BOOK_ID))).thenThrow(
            BookDomainException.bookNotFound(NON_EXISTENT_BOOK_ID),
        )

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                deleteBookController.deleteBook(UUID.fromString(NON_EXISTENT_BOOK_ID))
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, NON_EXISTENT_BOOK_ID), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }
}
