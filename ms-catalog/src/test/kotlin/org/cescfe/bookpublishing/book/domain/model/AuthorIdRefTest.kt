package org.cescfe.bookpublishing.book.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class AuthorIdRefTest {
    @Test
    fun `should create AuthorIdRef from UUID`() {
        // Given
        val uuid = UUID.randomUUID()

        // When
        val authorIdRef = AuthorIdRef(uuid)

        // Then
        assertEquals(uuid, authorIdRef.value)
    }

    @Test
    fun `should create AuthorIdRef from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val authorIdRef = AuthorIdRef.fromString(uuidString)

        // Then
        assertEquals(uuidString, authorIdRef.value.toString())
    }
}
