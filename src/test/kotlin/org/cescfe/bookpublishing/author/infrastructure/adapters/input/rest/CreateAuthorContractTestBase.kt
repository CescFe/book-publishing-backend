package org.cescfe.bookpublishing.author.infrastructure.adapters.input.rest

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.cescfe.bookpublishing.author.application.port.input.CreateAuthorUseCase
import org.cescfe.bookpublishing.author.objectMothers.AuthorObjectMother
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(CreateAuthorController::class)
@ActiveProfiles("contract-test")
abstract class CreateAuthorContractTestBase {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createAuthorUseCase: CreateAuthorUseCase

    @BeforeEach
    fun setup(context: WebApplicationContext) {
        RestAssuredMockMvc.mockMvc(mockMvc)

        val mockAuthor = AuthorObjectMother.createWithMultipleRoles()
        whenever(createAuthorUseCase.execute(any())).thenReturn(mockAuthor)
    }
}
