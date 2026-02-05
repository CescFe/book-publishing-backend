package org.cescfe.bookpublishing.auth.domain.policy

import org.cescfe.bookpublishing.auth.domain.model.enum.Role

class ScopePolicy {
    fun scopeFor(roles: Set<Role>): String =
        if (roles.contains(Role.ADMIN)) {
            "read write delete"
        } else {
            "read"
        }
}
