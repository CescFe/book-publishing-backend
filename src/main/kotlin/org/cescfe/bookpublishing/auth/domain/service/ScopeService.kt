package org.cescfe.bookpublishing.auth.domain.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class ScopeService {

    fun getScopeFromAuthorities(authorities: Collection<GrantedAuthority>): String {
        val roles = authorities.map { it.authority }

        return when {
            roles.contains("ROLE_ADMIN") -> "read write delete"
            roles.contains("ROLE_USER") -> "read"
            else -> "read"
        }
    }
}
