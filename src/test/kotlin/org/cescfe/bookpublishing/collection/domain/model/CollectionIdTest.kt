package org.cescfe.bookpublishing.collection.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class CollectionIdTest {
    @Test
    fun `should generate unique CollectionId`() {
        // When
        val id1 = CollectionId.generate()
        val id2 = CollectionId.generate()

        // Then
        assertEquals(UUID::class.java, id1.value.javaClass)
        assertEquals(UUID::class.java, id2.value.javaClass)
        assert(id1 != id2)
    }

    @Test
    fun `should create CollectionId from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val id = CollectionId.fromString(uuidString)

        // Then
        assertEquals(uuidString, id.value.toString())
    }
}
