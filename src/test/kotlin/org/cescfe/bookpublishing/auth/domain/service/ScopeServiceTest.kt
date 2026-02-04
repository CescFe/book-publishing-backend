package org.cescfe.bookpublishing.auth.domain.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class ScopeServiceTest {
    private lateinit var scopeService: ScopeService

    @BeforeEach
    fun setup() {
        scopeService = ScopeService()
    }

    @Test
    fun `should return read write delete for ADMIN role`() {
        // Given
        val authorities: Collection<GrantedAuthority> =
            listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
            )

        // When
        val scope = scopeService.getScopeFromAuthorities(authorities)

        // Then
        assertEquals("read write delete", scope)
    }

    @Test
    fun `should return read for USER role`() {
        // Given
        val authorities: Collection<GrantedAuthority> =
            listOf(
                SimpleGrantedAuthority("ROLE_USER"),
            )

        // When
        val scope = scopeService.getScopeFromAuthorities(authorities)

        // Then
        assertEquals("read", scope)
    }

    @Test
    fun `should return read for empty authorities`() {
        // Given
        val authorities: Collection<GrantedAuthority> = emptyList()

        // When
        val scope = scopeService.getScopeFromAuthorities(authorities)

        // Then
        assertEquals("read", scope)
    }

    @Test
    fun `should return read write delete when both ADMIN and USER roles present`() {
        // Given
        val authorities: Collection<GrantedAuthority> =
            listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_USER"),
            )

        // When
        val scope = scopeService.getScopeFromAuthorities(authorities)

        // Then
        assertEquals("read write delete", scope)
    }
}
