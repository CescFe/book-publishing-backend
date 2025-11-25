package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class CreateAuthorControllerTest {
    private lateinit var createAuthorUseCase: CreateAuthorUseCase
    private lateinit var mapper: AuthorRestMapper
    private lateinit var createAuthorController: CreateAuthorController

    companion object{
        const val NON_EXISTENT_AUTHOR_ID = "123e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        createAuthorUseCase = mock()
        mapper = mock()
        createAuthorController = CreateAuthorController(createAuthorUseCase, mapper)
    }

    @Test
    fun `should throw AuthorDomainException when full name is blank`() {
        // Given
        val requestDTO =
            CreateAuthorRequestDTO(
                fullName = "",
            )

        whenever(createAuthorUseCase.execute(CreateAuthorUseCase.Command(NON_EXISTENT_AUTHOR_ID))).thenThrow(
            AuthorDomainException.fullNameCannotBeBlank(),
        )

        // When & Then
        assertThrows<AuthorDomainException> {
            createAuthorController.createAuthor(requestDTO)
        }
    }
}
