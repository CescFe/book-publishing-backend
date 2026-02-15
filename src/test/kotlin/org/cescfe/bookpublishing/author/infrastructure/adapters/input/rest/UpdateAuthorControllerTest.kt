package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.UpdateAuthorRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.net.URI
import java.util.UUID
import kotlin.test.assertEquals

class UpdateAuthorControllerTest {
    private lateinit var updateAuthorUseCase: UpdateAuthorUseCase
    private lateinit var updateAuthorController: UpdateAuthorController

    companion object {
        private const val NOT_FOUND_MESSAGE = "Author with id %s not found"
        private const val NOT_FOUND_SUBTYPE = "AUTHOR_NOT_FOUND"
        private const val NON_EXISTENT_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        updateAuthorUseCase = mock()
        updateAuthorController = UpdateAuthorController(updateAuthorUseCase)
    }

    @Test
    fun `should throw AuthorDomainException when updating non-existent author`() {
        // Given
        val authorId = UUID.fromString(NON_EXISTENT_AUTHOR_ID)
        val requestDTO =
            UpdateAuthorRequestDTO(
                fullName = "Updated J.R.R. Tolkien",
                pseudonym = "Updated Tolkien",
                biography = "Updated English writer and philologist",
                email = "updated.tolkien@example.com",
                website = URI("https://www.updated-tolkiensociety.org"),
            )

        whenever(
            updateAuthorUseCase.execute(
                any(),
                any(),
            ),
        ).thenThrow(
            AuthorDomainException.authorNotFound(NON_EXISTENT_AUTHOR_ID),
        )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                updateAuthorController.updateAuthor(authorId, requestDTO)
            }
        assertEquals(String.format(NOT_FOUND_MESSAGE, authorId), exception.message)
        assertEquals(NOT_FOUND_SUBTYPE, exception.subType)
    }
}
