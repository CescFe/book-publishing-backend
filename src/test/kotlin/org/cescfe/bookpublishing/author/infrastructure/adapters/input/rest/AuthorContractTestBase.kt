package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.DeleteAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.GetAuthorUseCase
import org.cescfe.bookpublishing.author.application.port.input.ListAuthorsUseCase
import org.cescfe.bookpublishing.author.domain.model.AuthorRole
import org.cescfe.bookpublishing.author.domain.model.PaginatedResult
import org.cescfe.bookpublishing.author.domain.model.PaginationMeta
import org.cescfe.bookpublishing.author.infrastructure.adapters.DeleteAuthorController
import org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest.mapper.AuthorRestMapper
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
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
import java.util.UUID

@WebMvcTest(
    CreateAuthorController::class,
    GetAuthorController::class,
    ListAuthorsController::class,
    DeleteAuthorController::class,
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

    @BeforeEach
    fun setup(context: WebApplicationContext) {
        RestAssuredMockMvc.mockMvc(mockMvc)

        val mockAuthorForCreate = AuthorObjectMother.createWithMultipleRoles()
        whenever(createAuthorUseCase.execute(any())).thenReturn(mockAuthorForCreate)

        val specificId = UUID.fromString("477537ff-7e8b-4930-bd41-d7f3589120b1")
        val mockAuthorTolkien =
            AuthorObjectMother.create(
                id = specificId,
                fullName = "J.R.R. Tolkien",
                roles = setOf(AuthorRole.AUTHOR, AuthorRole.ILLUSTRATOR),
                pseudonym = "Tolkien",
                biography = "English writer and philologist",
                email = "tolkien@example.com",
                website = "https://www.tolkiensociety.org",
            )
        whenever(getAuthorUseCase.execute(any())).thenReturn(mockAuthorTolkien)

        val mockMinimalAuthor =
            AuthorObjectMother.create(
                id = UUID.fromString("12345678-1234-1234-1234-123456789012"),
                fullName = "Minimal Author",
                roles = setOf(AuthorRole.AUTHOR),
            )

        val authorsList = listOf(mockAuthorTolkien, mockMinimalAuthor)
        val paginatedResult =
            PaginatedResult(
                data = authorsList,
                meta =
                    PaginationMeta(
                        total = 2,
                        page = 1,
                        limit = 20,
                        totalPages = 1,
                    ),
            )
        whenever(listAuthorsUseCase.execute(any())).thenReturn(paginatedResult)

        doAnswer { }.whenever(deleteAuthorUseCase).execute(any())
    }
}
