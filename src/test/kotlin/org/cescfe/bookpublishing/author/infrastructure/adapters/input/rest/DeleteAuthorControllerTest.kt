package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

class DeleteAuthorControllerTest {
    private lateinit var deleteAuthorUseCase: DeleteAuthorUseCase
    private lateinit var deleteAuthorController: DeleteAuthorController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Author with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "AUTHOR_NOT_FOUND"
        private const val NON_EXISTENT_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        deleteAuthorUseCase = mock()
        deleteAuthorController = DeleteAuthorController(deleteAuthorUseCase)
    }

    @Test
    fun `should throw AuthorDomainException when deleting non-existent author`() {
        // Given
        whenever(deleteAuthorUseCase.execute(DeleteAuthorUseCase.Command(NON_EXISTENT_AUTHOR_ID))).thenThrow(
            AuthorDomainException.authorNotFound(NON_EXISTENT_AUTHOR_ID),
        )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                deleteAuthorController.deleteAuthor(UUID.fromString(NON_EXISTENT_AUTHOR_ID))
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, NON_EXISTENT_AUTHOR_ID), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }
}
