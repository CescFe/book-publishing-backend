package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.util.ReflectionTestUtils
import java.time.Instant

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
        Assertions.assertTrue(token.isNotEmpty())
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
        Assertions.assertEquals("user", username)
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
        Assertions.assertTrue(isValid)
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
        Assertions.assertFalse(isValid)
    }

    @Test
    fun `should return expiration time`() {
        // When
        val expiration = jwtUtil.getExpirationTime()

        // Then
        Assertions.assertEquals(3600L, expiration)
    }

    @Test
    fun `should get expiration instant from token`() {
        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")
        val token = jwtUtil.generateToken(userDetails)

        val expirationInstant = jwtUtil.getExpirationInstant(token)

        Assertions.assertTrue(expirationInstant.isAfter(Instant.now()))
    }

    @Test
    fun `should fail when secret is too short`() {
        // Given
        val badJwtUtil = JwtUtil()
        ReflectionTestUtils.setField(badJwtUtil, "secret", "too-short-secret")
        ReflectionTestUtils.setField(badJwtUtil, "expiration", 3600L)

        val userDetails = mock<UserDetails>()
        whenever(userDetails.username).thenReturn("user")

        // When / Then
        val ex =
            assertThrows<IllegalArgumentException> {
                badJwtUtil.generateToken(userDetails)
            }
        Assertions.assertEquals("JWT secret must be at least 32 characters long for security", ex.message)
    }
}
