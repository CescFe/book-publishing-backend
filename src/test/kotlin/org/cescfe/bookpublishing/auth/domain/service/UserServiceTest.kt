package org.cescfe.bookpublishing.auth.domain.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        passwordEncoder = mock()
        whenever(passwordEncoder.encode(any())).thenReturn("encodedPassword")
        userService = UserService(passwordEncoder)
    }

    @Test
    fun `should load admin user with correct roles`() {
        // Given
        val username = "admin@example.com"

        // When
        val userDetails = userService.loadUserByUsername(username)

        // Then
        assertEquals(username, userDetails.username)
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_USER")))
    }

    @Test
    fun `should load regular user with correct roles`() {
        // Given
        val username = "user@example.com"

        // When
        val userDetails = userService.loadUserByUsername(username)

        // Then
        assertEquals(username, userDetails.username)
        assertTrue(userDetails.authorities.contains(SimpleGrantedAuthority("ROLE_USER")))
        assertEquals(1, userDetails.authorities.size)
    }

    @Test
    fun `should throw exception for unknown user`() {
        // Given
        val username = "unknown@example.com"

        // When & Then
        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername(username)
        }
    }
}
