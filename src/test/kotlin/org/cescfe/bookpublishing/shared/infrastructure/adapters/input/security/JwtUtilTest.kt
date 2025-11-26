package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.util.ReflectionTestUtils

class JwtUtilTest {
    private lateinit var jwtUtil: JwtUtil

    @BeforeEach
    fun setup() {
        jwtUtil = JwtUtil()
        ReflectionTestUtils.setField(jwtUtil, "secret", "mySecretKeyThatIsAtLeast32CharactersLong")
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600L)
    }

    @Test
    fun `should generate token`() {
        // Given
        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")

        // When
        val token = jwtUtil.generateToken(userDetails)

        // Then
        assertTrue(token.isNotEmpty())
    }

    @Test
    fun `should get username from token`() {
        // Given
        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")
        val token = jwtUtil.generateToken(userDetails)

        // When
        val username = jwtUtil.getUsernameFromToken(token)

        // Then
        assertEquals("user", username)
    }

    @Test
    fun `should validate token`() {
        // Given
        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")
        val token = jwtUtil.generateToken(userDetails)

        // When
        val isValid = jwtUtil.validateToken(token, userDetails)

        // Then
        assertTrue(isValid)
    }

    @Test
    fun `should fail validation for different user`() {
        // Given
        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")
        val token = jwtUtil.generateToken(userDetails)

        val otherUserDetails = mock<UserDetails>()
        whenever(otherUserDetails.username).thenReturn("other")

        // When
        val isValid = jwtUtil.validateToken(token, otherUserDetails)

        // Then
        assertFalse(isValid)
    }
}
