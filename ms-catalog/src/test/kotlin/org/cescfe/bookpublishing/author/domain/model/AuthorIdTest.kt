package org.cescfe.bookpublishing.author.domain.model

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class AuthorIdTest {
    @Test
    fun `should generate unique AuthorId`() {
        // When
        val id1 = AuthorId.generate()
        val id2 = AuthorId.generate()

        // Then
        assertEquals(UUID::class.java, id1.value.javaClass)
        assertEquals(UUID::class.java, id2.value.javaClass)
        assert(id1 != id2)
    }

    @Test
    fun `should create AuthorId from string`() {
        // Given
        val uuidString = "da420b0a-64aa-470d-991e-7fcb7a936229"

        // When
        val authorId = AuthorId.fromString(uuidString)

        // Then
        assertEquals(uuidString, authorId.value.toString())
    }
}
