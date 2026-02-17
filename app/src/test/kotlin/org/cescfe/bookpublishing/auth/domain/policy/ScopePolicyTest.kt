package org.cescfe.bookpublishing.auth.domain.policy

import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScopePolicyTest {
    private val policy = ScopePolicy()

    @Test
    fun `should return admin scope when admin role present`() {
        val scope = policy.scopeFor(setOf(Role.ADMIN))

        assertEquals("read write delete", scope)
    }

    @Test
    fun `should return read scope when admin role missing`() {
        val scope = policy.scopeFor(setOf(Role.USER))

        assertEquals("read", scope)
    }
}
