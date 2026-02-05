package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthorityRoleMapperTest {
    private val mapper = AuthorityRoleMapper()

    @Test
    fun `should map role authorities to domain roles`() {
        val roles =
            mapper.mapAuthorities(
                listOf(
                    SimpleGrantedAuthority("ROLE_ADMIN"),
                    SimpleGrantedAuthority("ROLE_USER"),
                ),
            )

        assertEquals(setOf(Role.ADMIN, Role.USER), roles)
    }

    @Test
    fun `should ignore unknown roles`() {
        val roles =
            mapper.mapAuthorities(
                listOf(
                    SimpleGrantedAuthority("ROLE_ADMIN"),
                    SimpleGrantedAuthority("ROLE_UNKNOWN"),
                ),
            )

        assertEquals(setOf(Role.ADMIN), roles)
    }

    @Test
    fun `should handle non role authorities`() {
        val roles =
            mapper.mapAuthorities(
                listOf(
                    SimpleGrantedAuthority("SCOPE_read"),
                ),
            )

        assertEquals(emptySet<Role>(), roles)
    }
}
