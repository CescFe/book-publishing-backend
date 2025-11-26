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
    private val usersCache: Map<String, UserDetails> by lazy {
        authProperties.users
            .mapNotNull { userConfig ->
                val username = userConfig.username ?: return@mapNotNull null
                val password = userConfig.password ?: return@mapNotNull null

                val authorities = userConfig.roles
                    .map { SimpleGrantedAuthority("ROLE_$it") }

                username to User
                    .withUsername(username)
                    .password(passwordEncoder.encode(password))
                    .authorities(authorities)
                    .build()
            }
            .toMap()
    }

    override fun loadUserByUsername(username: String): UserDetails =
        usersCache[username]
            ?: throw UsernameNotFoundException("User not found: $username")
}
