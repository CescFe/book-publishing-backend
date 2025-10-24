package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.application.port.input.UpdateAuthorUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorSummary
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.cescfe.bookpublishing.author.objectMothers.AuthorSummaryObjectMother
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtRequestFilter
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtUtil
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(
    CreateAuthorController::class,
    GetAuthorController::class,
    ListAuthorsController::class,
    DeleteAuthorController::class,
    UpdateAuthorController::class,
    AuthorRestMapper::class,
)
@ActiveProfiles("contract-test")
abstract class AuthorContractTestBase {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createAuthorUseCase: CreateAuthorUseCase

    @MockitoBean
    private lateinit var getAuthorUseCase: GetAuthorUseCase

    @MockitoBean
    private lateinit var listAuthorsUseCase: ListAuthorsUseCase

    @MockitoBean
    private lateinit var deleteAuthorUseCase: DeleteAuthorUseCase

    @MockitoBean
    private lateinit var updateAuthorUseCase: UpdateAuthorUseCase

    @MockitoBean
    private lateinit var jwtRequestFilter: JwtRequestFilter

    @MockitoBean
    private lateinit var jwtUtil: JwtUtil

    @BeforeEach
    fun setup(context: WebApplicationContext) {
        RestAssuredMockMvc.mockMvc(mockMvc)

        whenever(jwtUtil.validateToken(any(), any())).thenReturn(true)
        whenever(jwtUtil.getUsernameFromToken(any())).thenReturn("user@example.com")

        // CreateAuthor Setup
        val mockAuthorForCreate = AuthorObjectMother.createWithMultipleRoles()
        whenever(createAuthorUseCase.execute(any())).thenReturn(mockAuthorForCreate)

        // GetAuthor Setup
        val mockAuthorTolkien = AuthorObjectMother.createForGetContractTest()
        whenever(getAuthorUseCase.execute(any())).thenReturn(mockAuthorTolkien)

        // ListAuthors Setup
        val authors =
            listOf(
                AuthorSummaryObjectMother.createFirstAuthorSummary(),
                AuthorSummaryObjectMother.createSecondAuthorSummary(),
            )
        whenever(listAuthorsUseCase.execute(any())).thenReturn(authors.toPaginatedResult())

        // DeleteAuthor Setup
        doAnswer { }.whenever(deleteAuthorUseCase).execute(any())

        // UpdateAuthor Setup
        val updatedAuthor = AuthorObjectMother.createForUpdateContractTest()
        whenever(updateAuthorUseCase.execute(any())).thenReturn(updatedAuthor)
    }

    private fun List<AuthorSummary>.toPaginatedResult(
        page: Int = 1,
        limit: Int = 20,
    ): PaginatedResult<AuthorSummary> =
        PaginatedResult(
            data = this,
            meta =
                PaginationMeta(
                    total = size.toLong(),
                    page = page,
                    limit = limit,
                    totalPages = if (isEmpty()) 0 else ((size - 1) / limit + 1),
                ),
        )
}
