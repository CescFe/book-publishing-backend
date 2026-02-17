package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import jakarta.servlet.FilterChain
import org.cescfe.bookpublishing.auth.application.port.output.TokenPayload
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.time.Instant
import java.util.UUID

class JwtRequestFilterTest {
    private lateinit var userDetailsService: UserDetailsService
    private lateinit var tokenService: TokenService
    private lateinit var authenticationEntryPoint: JwtAuthenticationEntryPoint
    private lateinit var jwtRequestFilter: JwtRequestFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setup() {
        userDetailsService = mock()
        tokenService = mock()
        authenticationEntryPoint = mock()
        jwtRequestFilter = JwtRequestFilter(userDetailsService, tokenService, authenticationEntryPoint)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        filterChain = mock()
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should set authentication for valid token`() {
        val token = "valid.jwt.token"
        val username = "user"
        val userDetails: UserDetails =
            User
                .withUsername(username)
                .password("password")
                .authorities("ROLE_USER")
                .build()

        request.addHeader("Authorization", "Bearer $token")
        whenever(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails)
        whenever(tokenService.parseToken(token)).thenReturn(
            TokenPayload(
                userId = UserId(UUID.randomUUID()),
                username = Username(username),
                roles = setOf(Role.USER),
                permissions = emptySet(),
                scope = "read",
                expiresAt = Instant.now().plusSeconds(3600),
            ),
        )

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication != null)
        assert(SecurityContextHolder.getContext().authentication.name == username)
    }

    @Test
    fun `should not set authentication when no Authorization header`() {
        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        verify(tokenService, never()).parseToken(any())
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not set authentication for invalid token`() {
        val token = "invalid.jwt.token"
        request.addHeader("Authorization", "Bearer $token")
        whenever(tokenService.parseToken(token)).thenThrow(RuntimeException("Invalid token"))

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(authenticationEntryPoint).commence(any(), any(), any())
        verify(filterChain, never()).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not set authentication for expired token`() {
        val token = "expired.jwt.token"
        val username = "user"
        request.addHeader("Authorization", "Bearer $token")
        whenever(tokenService.parseToken(token)).thenReturn(
            TokenPayload(
                userId = UserId(UUID.randomUUID()),
                username = Username(username),
                roles = emptySet(),
                permissions = emptySet(),
                scope = "",
                expiresAt = Instant.now().minusSeconds(10),
            ),
        )

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(authenticationEntryPoint).commence(any(), any(), any())
        verify(filterChain, never()).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not set authentication when token does not start with Bearer`() {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz")

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        verify(tokenService, never()).parseToken(any())
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should continue filter chain when token parsing throws exception`() {
        val token = "malformed.token"
        request.addHeader("Authorization", "Bearer $token")
        whenever(tokenService.parseToken(token)).thenThrow(RuntimeException("Invalid token"))

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(authenticationEntryPoint).commence(any(), any(), any())
        verify(filterChain, never()).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not override existing authentication`() {
        val token = "valid.jwt.token"
        val existingAuth = UsernamePasswordAuthenticationToken("existing", null, emptyList())

        request.addHeader("Authorization", "Bearer $token")
        SecurityContextHolder.getContext().authentication = existingAuth

        jwtRequestFilter.doFilter(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
        verify(tokenService, never()).parseToken(any())
        verify(userDetailsService, never()).loadUserByUsername(any())
        assert(SecurityContextHolder.getContext().authentication === existingAuth)
    }
}
