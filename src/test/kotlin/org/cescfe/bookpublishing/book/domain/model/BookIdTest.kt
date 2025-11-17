package org.cescfe.bookpublishing.book.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class BookIdTest {
    @Test
    fun `should generate unique BookId`() {
        // When
        val id1 = BookId.generate()
        val id2 = BookId.generate()

        // Then
        assertEquals(UUID::class.java, id1.value.javaClass)
        assertEquals(UUID::class.java, id2.value.javaClass)
        assert(id1 != id2)
    }

    @Test
    fun `should create BookId from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val id = BookId.fromString(uuidString)

        // Then
        assertEquals(uuidString, id.value.toString())
    }
}
