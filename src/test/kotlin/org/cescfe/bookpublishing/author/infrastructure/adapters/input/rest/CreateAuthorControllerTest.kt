package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.exception.AuthorDomainException
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateAuthorRequestDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class CreateAuthorControllerTest {
    private lateinit var createAuthorUseCase: CreateAuthorUseCase
    private lateinit var mapper: AuthorRestMapper
    private lateinit var createAuthorController: CreateAuthorController

    @BeforeEach
    fun setup() {
        createAuthorUseCase = org.mockito.kotlin.mock()
        mapper = org.mockito.kotlin.mock()
        createAuthorController = CreateAuthorController(createAuthorUseCase, mapper)
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
        assertThrows<AuthorDomainException> {
            createAuthorController.createAuthor(requestDTO)
        }
    }
}
