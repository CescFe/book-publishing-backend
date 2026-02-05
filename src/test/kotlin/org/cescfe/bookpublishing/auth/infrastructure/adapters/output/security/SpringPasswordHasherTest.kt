package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security

import org.cescfe.bookpublishing.auth.domain.model.PasswordHash
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder

class SpringPasswordHasherTest {
    @Test
    fun `should hash raw password using encoder`() {
        val passwordEncoder = mock<PasswordEncoder>()
        whenever(passwordEncoder.encode("secret")).thenReturn("encoded-secret")
        val hasher = SpringPasswordHasher(passwordEncoder)

        val result = hasher.hash("secret")

        assertEquals(PasswordHash("encoded-secret"), result)
    }

    @Test
    fun `should match raw password using encoder`() {
        val passwordEncoder = mock<PasswordEncoder>()
        whenever(passwordEncoder.matches("secret", "encoded-secret")).thenReturn(true)
        val hasher = SpringPasswordHasher(passwordEncoder)

        val matches = hasher.matches("secret", PasswordHash("encoded-secret"))

        assertTrue(matches)
    }
}
