package org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest

import org.cescfe.bookpublishing.collection.application.port.input.UpdateCollectionUseCase
import org.cescfe.bookpublishing.collection.infrastructure.adapters.input.rest.mapper.CollectionRestMapper
import org.cescfe.bookpublishing.collection.objectMothers.CollectionObjectMother
import org.cescfe.bookpublishing.infrastructure.openapi.http.inbound.model.CreateCollection201ResponseDTO
import org.cescfe.bookpublishing.shared.domain.model.enum.Genre
import org.cescfe.bookpublishing.shared.domain.model.enum.Language
import org.cescfe.bookpublishing.shared.domain.model.enum.ReadingLevel
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.rest.TestResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.ZoneOffset
import java.util.UUID
import kotlin.test.assertEquals

@WebMvcTest(controllers = [UpdateCollectionController::class])
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [UpdateCollectionController::class])
class UpdateCollectionControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateCollectionUseCase: UpdateCollectionUseCase

    @MockitoBean
    private lateinit var mapper: CollectionRestMapper

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

        whenever(mapper.toDto(any())).thenAnswer { invocation ->
            val collection = invocation.getArgument<org.cescfe.bookpublishing.collection.domain.model.Collection>(0)

            CreateCollection201ResponseDTO(
                id = collection.id.value,
                name = collection.name.value,
                readingLevel =
                    collection.readingLevel?.let {
                        CreateCollection201ResponseDTO.ReadingLevel.valueOf(it.name)
                    },
                primaryLanguage =
                    collection.primaryLanguage?.let {
                        CreateCollection201ResponseDTO.PrimaryLanguage.valueOf(it.name)
                    },
                secondaryLanguages =
                    collection.secondaryLanguages?.value?.map { lang ->
                        CreateCollection201ResponseDTO.SecondaryLanguages.valueOf(lang.name)
                    },
                primaryGenre =
                    collection.primaryGenre?.let {
                        CreateCollection201ResponseDTO.PrimaryGenre.valueOf(it.name)
                    },
                secondaryGenres =
                    collection.secondaryGenres?.value?.map { genre ->
                        CreateCollection201ResponseDTO.SecondaryGenres.valueOf(genre.name)
                    },
                createdAt = collection.audit?.createdAt?.atOffset(ZoneOffset.UTC),
                createdBy = collection.audit?.createdBy,
                updatedAt = collection.audit?.updatedAt?.atOffset(ZoneOffset.UTC),
                updatedBy = collection.audit?.updatedBy,
            )
        }
    }

    @Test
    fun `maps http request to UpdateCollectionCommand correctly`() {
        // Given
        val requestBody = TestResources.json("payloads/collections/update-collection-request.json")

        // When / Then
        mockMvc
            .perform(
                put(URI, TEST_COLLECTION_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(TEST_COLLECTION_ID))
            .andExpect(jsonPath("$.name").value("Updated Collection Name"))
            .andExpect(jsonPath("$.reading_level").value("ADULT"))
            .andExpect(jsonPath("$.primary_language").value("ENGLISH"))
            .andExpect(jsonPath("$.secondary_languages[0]").value("CATALAN"))
            .andExpect(jsonPath("$.secondary_languages[1]").value("SPANISH"))
            .andExpect(jsonPath("$.primary_genre").value("FANTASY"))
            .andExpect(jsonPath("$.secondary_genres[0]").value("ADVENTURE"))
            .andExpect(jsonPath("$.secondary_genres[1]").doesNotExist())

        val commandCaptor = argumentCaptor<UpdateCollectionUseCase.Command>()
        verify(updateCollectionUseCase).execute(any(), commandCaptor.capture())

        val command = commandCaptor.firstValue
        assertEquals("Updated Collection Name", command.name)
        assertEquals(ReadingLevel.ADULT, command.readingLevel)
        assertEquals(Language.ENGLISH, command.primaryLanguage)
        assertEquals(listOf(Language.CATALAN, Language.SPANISH), command.secondaryLanguages)
        assertEquals(Genre.FANTASY, command.primaryGenre)
        assertEquals(listOf(Genre.ADVENTURE), command.secondaryGenres)
    }

    companion object {
        private const val URI = "/api/v1/collections/{id}"
        private const val TEST_COLLECTION_ID = "223e4567-e89b-12d3-a456-426614174000"
    }
}
