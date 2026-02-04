package org.cescfe.bookpublishing.auth.application.port.input.interactor

import org.cescfe.bookpublishing.auth.application.port.input.LoginUseCase
import org.cescfe.bookpublishing.auth.domain.service.ScopeService
import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security.UserService
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class LoginImplTest {
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var jwtUtil: JwtUtil
    private lateinit var userService: UserService
    private lateinit var scopeService: ScopeService
    private lateinit var loginImpl: LoginImpl

    @BeforeEach
    fun setup() {
        authenticationManager = mock()
        jwtUtil = mock()
        userService = mock()
        scopeService = mock()
        loginImpl = LoginImpl(authenticationManager, jwtUtil, userService, scopeService)
    }

    @Test
    fun `should return access token on successful authentication`() {
        // Given
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "password123",
            )
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val userDetails: UserDetails =
            User
                .withUsername(input.username)
                .password("encodedPassword")
                .authorities(authorities)
                .build()
        val authentication: Authentication = mock()
        val expectedToken = "jwt.token.here"
        val expectedScope = "read"
        val expectedExpiration = 3600L

        whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
            .thenReturn(authentication)
        whenever(userService.loadUserByUsername(input.username)).thenReturn(userDetails)
        whenever(jwtUtil.generateToken(userDetails)).thenReturn(expectedToken)
        whenever(jwtUtil.getExpirationTime()).thenReturn(expectedExpiration)
        whenever(scopeService.getScopeFromAuthorities(any())).thenReturn(expectedScope)

        // When
        val result = loginImpl.execute(input)

        // Then
        assertEquals(expectedToken, result.accessToken)
        assertEquals(expectedExpiration, result.expiresIn)
        assertEquals(expectedScope, result.scope)
        assertNotNull(result.userId)
    }

    @Test
    fun `should throw exception for invalid credentials`() {
        // Given
        val input =
            LoginUseCase.InputValues(
                username = "user@example.com",
                password = "wrongPassword",
            )

        whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
            .thenThrow(BadCredentialsException("Bad credentials"))

        // When & Then
        assertThrows<BadCredentialsException> {
            loginImpl.execute(input)
        }
    }

    @Test
    fun `should include correct scope based on user role`() {
        // Given
        val input =
            LoginUseCase.InputValues(
                username = "admin@example.com",
                password = "admin123",
            )
        val authorities =
            listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_USER"),
            )
        val userDetails: UserDetails =
            User
                .withUsername(input.username)
                .password("encodedPassword")
                .authorities(authorities)
                .build()
        val authentication: Authentication = mock()
        val expectedScope = "read write delete"

        whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
            .thenReturn(authentication)
        whenever(userService.loadUserByUsername(input.username)).thenReturn(userDetails)
        whenever(jwtUtil.generateToken(userDetails)).thenReturn("token")
        whenever(jwtUtil.getExpirationTime()).thenReturn(3600L)
        whenever(scopeService.getScopeFromAuthorities(any())).thenReturn(expectedScope)

        // When
        val result = loginImpl.execute(input)

        // Then
        assertEquals(expectedScope, result.scope)
    }
}
