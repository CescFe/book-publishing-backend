package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security

import jakarta.servlet.FilterChain
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

class JwtRequestFilterTest {
    private lateinit var userDetailsService: UserDetailsService
    private lateinit var jwtUtil: JwtUtil
    private lateinit var jwtRequestFilter: JwtRequestFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setup() {
        userDetailsService = mock()
        jwtUtil = mock()
        jwtRequestFilter = JwtRequestFilter(userDetailsService, jwtUtil)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        filterChain = mock()
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should set authentication for valid token`() {
        // Given
        val token = "valid.jwt.token"
        val username = "user"
        val userDetails: UserDetails =
            User
                .withUsername(username)
                .password("password")
                .authorities("ROLE_USER")
                .build()

        request.addHeader("Authorization", "Bearer $token")
        whenever(jwtUtil.getUsernameFromToken(token)).thenReturn(username)
        whenever(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails)
        whenever(jwtUtil.validateToken(token, userDetails)).thenReturn(true)

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication != null)
        assert(SecurityContextHolder.getContext().authentication.name == username)
    }

    @Test
    fun `should not set authentication when no Authorization header`() {
        // Given - no Authorization header set

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        verify(jwtUtil, never()).getUsernameFromToken(any())
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not set authentication for invalid token`() {
        // Given
        val token = "invalid.jwt.token"
        val username = "user"
        val userDetails: UserDetails =
            User
                .withUsername(username)
                .password("password")
                .authorities("ROLE_USER")
                .build()

        request.addHeader("Authorization", "Bearer $token")
        whenever(jwtUtil.getUsernameFromToken(token)).thenReturn(username)
        whenever(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails)
        whenever(jwtUtil.validateToken(token, userDetails)).thenReturn(false)

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not set authentication when token does not start with Bearer`() {
        // Given
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz")

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        verify(jwtUtil, never()).getUsernameFromToken(any())
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should continue filter chain when token parsing throws exception`() {
        // Given
        val token = "malformed.token"
        request.addHeader("Authorization", "Bearer $token")
        whenever(jwtUtil.getUsernameFromToken(token)).thenThrow(RuntimeException("Invalid token"))

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        assert(SecurityContextHolder.getContext().authentication == null)
    }

    @Test
    fun `should not override existing authentication`() {
        // Given
        val token = "valid.jwt.token"
        val username = "user"
        val existingAuth = UsernamePasswordAuthenticationToken("existing", null, emptyList())

        request.addHeader("Authorization", "Bearer $token")
        SecurityContextHolder.getContext().authentication = existingAuth
        whenever(jwtUtil.getUsernameFromToken(token)).thenReturn(username)

        // When
        jwtRequestFilter.doFilter(request, response, filterChain)

        // Then
        verify(filterChain).doFilter(request, response)
        verify(userDetailsService, never()).loadUserByUsername(any())
        verify(jwtUtil, never()).validateToken(any(), any())
        assert(SecurityContextHolder.getContext().authentication === existingAuth)
    }
}
