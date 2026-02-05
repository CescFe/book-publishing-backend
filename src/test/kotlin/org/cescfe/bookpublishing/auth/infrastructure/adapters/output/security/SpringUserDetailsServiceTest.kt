package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security

import org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config.AuthProperties
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

class SpringUserDetailsServiceTest {
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authProperties: AuthProperties
    private lateinit var userService: SpringUserDetailsService

    @BeforeEach
    fun setup() {
        passwordEncoder = mock()
        authProperties = mock()
        whenever(passwordEncoder.encode(any())).thenReturn("encodedPassword")
        userService = SpringUserDetailsService(passwordEncoder, authProperties)
    }

    @Test
    fun `should load admin user with correct roles`() {
        // Given
        val username = "admin@example.com"
        val userConfig = AuthProperties.User()
        userConfig.username = username
        userConfig.password = "admin123"
        userConfig.roles = listOf("ADMIN", "USER")

        whenever(authProperties.users).thenReturn(listOf(userConfig))

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
        val userConfig = AuthProperties.User()
        userConfig.username = username
        userConfig.password = "user123"
        userConfig.roles = listOf("USER")

        whenever(authProperties.users).thenReturn(listOf(userConfig))

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
        whenever(authProperties.users).thenReturn(emptyList())

        // When & Then
        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername(username)
        }
    }

    @Test
    fun `should ignore users with null username or password`() {
        // Given
        val missingUsername = AuthProperties.User()
        missingUsername.password = "pass"
        missingUsername.roles = listOf("USER")

        val missingPassword = AuthProperties.User()
        missingPassword.username = "nullpass@example.com"
        missingPassword.roles = listOf("USER")

        whenever(authProperties.users).thenReturn(listOf(missingUsername, missingPassword))

        // When & Then
        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername("nullpass@example.com")
        }
    }
}
