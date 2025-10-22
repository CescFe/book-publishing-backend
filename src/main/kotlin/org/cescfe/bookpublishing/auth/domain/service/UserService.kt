package org.cescfe.bookpublishing.auth.domain.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val users =
            mapOf(
                "admin@example.com" to passwordEncoder.encode("admin123"),
                "user@example.com" to passwordEncoder.encode("user123"),
            )

        val password =
            users[username]
                ?: throw UsernameNotFoundException("User not found: $username")

        val authorities = when (username) {
            "admin@example.com" -> listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_USER")
            )
            "user@example.com" -> listOf(
                SimpleGrantedAuthority("ROLE_USER")
            )
            else -> listOf(SimpleGrantedAuthority("ROLE_USER"))
        }

        return User
            .builder()
            .username(username)
            .password(password)
            .authorities(authorities)
            .build()
    }
}
