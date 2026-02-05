package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.springframework.security.core.GrantedAuthority

class AuthorityRoleMapper {
    fun mapAuthorities(authorities: Collection<GrantedAuthority>): Set<Role> =
        authorities
            .mapNotNull { authority ->
                val role = authority.authority.removePrefix("ROLE_")
                runCatching { Role.valueOf(role) }.getOrNull()
            }.toSet()
}
