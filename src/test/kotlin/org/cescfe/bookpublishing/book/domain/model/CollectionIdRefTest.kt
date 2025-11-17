package org.cescfe.bookpublishing.book.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class CollectionIdRefTest {
    @Test
    fun `should create CollectionIdRef from UUID`() {
        // Given
        val uuid = UUID.randomUUID()

        // When
        val collectionIdRef = CollectionIdRef(uuid)

        // Then
        assertEquals(uuid, collectionIdRef.value)
    }

    @Test
    fun `should create CollectionIdRef from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val collectionIdRef = CollectionIdRef.fromString(uuidString)

        // Then
        assertEquals(uuidString, collectionIdRef.value.toString())
    }
}
