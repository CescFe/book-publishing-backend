package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.ms.catalog.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateAuthorControllerTest {
    private lateinit var createAuthorUseCase: CreateAuthorUseCase
    private lateinit var createAuthorController: CreateAuthorController

    companion object {
        private const val ERROR_MESSAGE = "Full name cannot be blank"
        private const val ERROR_SUBTYPE = "FULL_NAME_CANNOT_BE_BLANK"
    }

    @BeforeEach
    fun setup() {
        createAuthorUseCase = mock()
        createAuthorController = CreateAuthorController(createAuthorUseCase)
    }

    @Test
    fun `should throw AuthorDomainException when full name is blank`() {
        // Given
        val requestDTO =
            CreateAuthorRequestDTO(
                fullName = "",
            )

        whenever(createAuthorUseCase.execute(any())).thenThrow(
            AuthorDomainException.fullNameCannotBeBlank(),
        )

        // When & Then
        val exception =
            assertThrows<AuthorDomainException> {
                createAuthorController.createAuthor(requestDTO)
            }
        assertEquals(ERROR_MESSAGE, exception.message)
        assertEquals(ERROR_SUBTYPE, exception.subType)
    }
}
