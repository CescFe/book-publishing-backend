package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.application.port.input.ListAllCollectionsUseCase
import org.cescfe.bookpublishing.collection.objectMothers.CollectionSummaryObjectMother
import org.cescfe.bookpublishing.shared.domain.model.NonPaginatedResult
import org.cescfe.bookpublishing.shared.domain.model.NonPaginationMeta
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "spring.autoconfigure.exclude=" +
            "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
            "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration," +
            "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration",
    ],
)
class ListAllCollectionsControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var listAllCollectionsUseCase: ListAllCollectionsUseCase

    companion object {
        private const val URI = "/api/v1/collections/all"
        private const val ROLE = "ROLE_USER"
        private const val COLLECTION_ID = "477537ff-7e8b-4930-bd41-d7f3589120b1"
    }

    @BeforeEach
    fun setup() {
        val testCollectionSummary = CollectionSummaryObjectMother.createFirstCollectionSummary()
        val result =
            NonPaginatedResult(
                data = listOf(testCollectionSummary),
                metadata =
                    NonPaginationMeta(
                        total = 1L,
                    ),
            )

        whenever(listAllCollectionsUseCase.execute()).thenReturn(result)
    }

    @Test
    fun `should list all collections successfully`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get(URI)
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE))),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(COLLECTION_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Fantasy Classics"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].reading_level").value("ADULT"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].primary_language").value("ENGLISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].primary_genre").value("FANTASY"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.total").value(1))
    }
}
