package org.cescfe.bookpublishing.book.domain.model

import org.cescfe.bookpublishing.book.domain.exception.BookDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class IllustratorIdsTest {
    @Test
    fun `should create illustrator ids with valid data`() {
        // Given
        val illustratorId1 = IllustratorIdRef(UUID.randomUUID())
        val illustratorId2 = IllustratorIdRef(UUID.randomUUID())

        // When
        val illustratorIds = IllustratorIds(listOf(illustratorId1, illustratorId2))

        // Then
        assertEquals(2, illustratorIds.value.size)
        assertEquals(illustratorId1, illustratorIds.value[0])
        assertEquals(illustratorId2, illustratorIds.value[1])
    }

    @Test
    fun `should create illustrator ids with empty list`() {
        // When
        val illustratorIds = IllustratorIds(emptyList())

        // Then
        assertEquals(0, illustratorIds.value.size)
    }

    @Test
    fun `should throw BookDomainException when illustrator ids contain duplicates`() {
        // Given
        val illustratorId = IllustratorIdRef(UUID.randomUUID())
        val duplicatedIllustratorIds = listOf(illustratorId, illustratorId)

        // When & Then
        val exception =
            assertThrows<BookDomainException> {
                IllustratorIds(duplicatedIllustratorIds)
            }
        assertEquals("Illustrator IDs cannot contain duplicates", exception.message)
    }
}
