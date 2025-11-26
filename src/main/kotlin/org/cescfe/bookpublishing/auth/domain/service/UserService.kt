package org.cescfe.bookpublishing.auth.domain.service

import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.config.AuthProperties
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
    private val authProperties: AuthProperties,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userConfig =
            authProperties.users.find { it.username == username }
                ?: throw UsernameNotFoundException("User not found: $username")

        val authorities =
            userConfig.roles.map { role ->
                SimpleGrantedAuthority("ROLE_$role")
            }

        return User
            .builder()
            .username(userConfig.username)
            .password(passwordEncoder.encode(userConfig.password))
            .authorities(authorities)
            .build()
    }
}
