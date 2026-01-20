package org.cescfe.bookpublishing.collection.infrastructure.adapters

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID
import kotlin.test.Test

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
class UpdateCollectionControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateCollectionUseCase: UpdateCollectionUseCase

    companion object {
        private const val URI = "/api/v1/collections/%s"
        private const val ROLE = "ROLE_ADMIN"
        private const val TEST_COLLECTION_ID = "223e4567-e89b-12d3-a456-426614174000"
    }

    @BeforeEach
    fun setup() {
        whenever(updateCollectionUseCase.execute(any(), any())).thenAnswer { invocation ->
            val collectionId = invocation.getArgument<String>(0)
            val command = invocation.getArgument<UpdateCollectionUseCase.Command>(1)

            CollectionObjectMother.create(
                id = UUID.fromString(collectionId),
                name = command.name,
                readingLevel = command.readingLevel,
                primaryLanguage = command.primaryLanguage,
                secondaryLanguages = command.secondaryLanguages,
                primaryGenre = command.primaryGenre,
                secondaryGenres = command.secondaryGenres,
            )
        }
    }

    @Test
    fun `should update collection successfully`() {
        val requestBody =
            """
            {
                "name": "Updated Collection Name",
                "reading_level": "ADULT",
                "primary_language": "ENGLISH",
                "secondary_languages": ["CATALAN", "SPANISH"],
                "primary_genre": "FANTASY",
                "secondary_genres": ["ADVENTURE"]
            }
            """.trimIndent()

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(URI.format(TEST_COLLECTION_ID))
                    .with(jwt().authorities(SimpleGrantedAuthority(ROLE)))
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_COLLECTION_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Collection Name"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.reading_level").value("ADULT"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_language").value("ENGLISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[0]").value("CATALAN"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[1]").value("SPANISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_genre").value("FANTASY"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[0]").value("ADVENTURE"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[1]").doesNotExist())
    }
}
