package org.cescfe.bookpublishing.collection.domain.model

import org.cescfe.bookpublishing.collection.domain.exception.CollectionDomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CollectionNameTest {
    @Test
    fun `should create collection name with valid data`() {
        // When
        val name = CollectionName("Fantasy Classics")

        // Then
        assertEquals("Fantasy Classics", name.value)
    }

    @Test
    fun `should throw CollectionDomainException when name is blank`() {
        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                CollectionName("   ")
            }
        assertEquals("Collection name cannot be blank", exception.message)
    }

    @Test
    fun `should throw CollectionDomainException when name is too long`() {
        // Given
        val longName = "a".repeat(81)

        // When & Then
        val exception =
            assertThrows<CollectionDomainException> {
                CollectionName(longName)
            }
        assertEquals("Collection name must be between 1 and 80 characters", exception.message)
    }

    @Test
    fun `should create collection name with exactly 80 characters`() {
        // Given
        val name80Chars = "a".repeat(80)

        // When
        val name = CollectionName(name80Chars)

        // Then
        assertEquals(80, name.value.length)
    }
}
