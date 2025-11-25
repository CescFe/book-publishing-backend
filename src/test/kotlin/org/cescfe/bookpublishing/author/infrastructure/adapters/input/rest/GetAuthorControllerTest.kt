package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.UUID

class GetAuthorControllerTest {
    private lateinit var getAuthorUseCase: GetAuthorUseCase
    private lateinit var mapper: AuthorRestMapper
    private lateinit var getAuthorController: GetAuthorController

    companion object {
        const val NON_EXISTENT_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        getAuthorUseCase = mock()
        mapper = mock()
        getAuthorController = GetAuthorController(getAuthorUseCase, mapper)
    }

    @Test
    fun `should throw AuthorDomainException when author does not exist`() {
        // Given
        whenever(getAuthorUseCase.execute(GetAuthorUseCase.Query(NON_EXISTENT_AUTHOR_ID))).thenThrow(
            AuthorDomainException.authorNotFound(NON_EXISTENT_AUTHOR_ID),
        )

        // When & Then
        assertThrows<AuthorDomainException> {
            getAuthorController.getAuthorByID(UUID.fromString(NON_EXISTENT_AUTHOR_ID))
        }
    }
}
