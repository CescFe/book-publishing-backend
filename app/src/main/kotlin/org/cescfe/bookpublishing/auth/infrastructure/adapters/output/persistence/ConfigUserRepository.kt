package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.persistence

import org.cescfe.bookpublishing.auth.application.port.output.PasswordHasher
import org.cescfe.bookpublishing.auth.application.port.output.UserRepository
import org.cescfe.bookpublishing.auth.domain.model.AuthUser
import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config.AuthProperties
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ConfigUserRepository(
    private val authProperties: AuthProperties,
    private val passwordHasher: PasswordHasher,
) : UserRepository {
    private val usersByUsername: Map<String, AuthUser> by lazy {
        authProperties.users
            .mapNotNull { userConfig ->
                val username = userConfig.username ?: return@mapNotNull null
                val password = userConfig.password ?: return@mapNotNull null

                val roles =
                    userConfig.roles
                        .mapNotNull { role ->
                            runCatching { Role.valueOf(role) }.getOrNull()
                        }.toSet()

                val account =
                    AuthUser(
                        id = UserId(UUID.nameUUIDFromBytes(username.toByteArray())),
                        username = Username(username),
                        passwordHash = passwordHasher.hash(password),
                        roles = roles,
                        permissions = emptySet(),
                    )

                username to account
            }.toMap()
    }

    override fun findById(id: UserId): AuthUser? = usersByUsername.values.firstOrNull { it.id == id }

    override fun findByUsername(username: Username): AuthUser? = usersByUsername[username.value]
}
