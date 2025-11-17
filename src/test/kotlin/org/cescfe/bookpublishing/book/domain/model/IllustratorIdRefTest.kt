package org.cescfe.bookpublishing.book.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class IllustratorIdRefTest {
    @Test
    fun `should create IllustratorIdRef from UUID`() {
        // Given
        val uuid = UUID.randomUUID()

        // When
        val illustratorIdRef = IllustratorIdRef(uuid)

        // Then
        assertEquals(uuid, illustratorIdRef.value)
    }

    @Test
    fun `should create IllustratorIdRef from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val illustratorIdRef = IllustratorIdRef.fromString(uuidString)

        // Then
        assertEquals(uuidString, illustratorIdRef.value.toString())
    }
}
