package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

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
        // Given
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

        // When
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(URI.format(TEST_COLLECTION_ID))
                    .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(SimpleGrantedAuthority(ROLE)))
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON),
            )
            // Then
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_COLLECTION_ID))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Collection Name"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.reading_level").value("ADULT"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_language").value("ENGLISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[0]").value("CATALAN"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_languages[1]").value("SPANISH"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.primary_genre").value("FANTASY"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[0]").value("ADVENTURE"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.secondary_genres[1]").doesNotExist())

        val commandCaptor = argumentCaptor<UpdateCollectionUseCase.Command>()
        verify(updateCollectionUseCase).execute(any(), commandCaptor.capture())

        val capturedCommand = commandCaptor.firstValue
        assertEquals("Updated Collection Name", capturedCommand.name)
        assertEquals(ReadingLevel.ADULT, capturedCommand.readingLevel)
        assertEquals(Language.ENGLISH, capturedCommand.primaryLanguage)
        assertEquals(listOf(Language.CATALAN, Language.SPANISH), capturedCommand.secondaryLanguages)
        assertEquals(Genre.FANTASY, capturedCommand.primaryGenre)
        assertEquals(listOf(Genre.ADVENTURE), capturedCommand.secondaryGenres)
    }

    companion object {
        private const val URI = "/api/v1/collections/%s"
        private const val ROLE = "ROLE_ADMIN"
        private const val TEST_COLLECTION_ID = "223e4567-e89b-12d3-a456-426614174000"
    }
}
