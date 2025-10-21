package org.cescfe.bookpublishing.auth.domain.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val users =
            mapOf(
                "admin@example.com" to "admin123",
                "user@example.com" to "user123",
            )

        val password =
            users[username]
                ?: throw UsernameNotFoundException("User not found: $username")

        return User
            .builder()
            .username(username)
            .password(password)
            .authorities(listOf(SimpleGrantedAuthority("ROLE_USER")))
            .build()
    }
}
