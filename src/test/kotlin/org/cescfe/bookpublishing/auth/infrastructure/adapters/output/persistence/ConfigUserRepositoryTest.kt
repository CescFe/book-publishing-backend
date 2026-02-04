package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.persistence

import org.cescfe.bookpublishing.auth.domain.model.UserId
import org.cescfe.bookpublishing.auth.domain.model.Username
import org.cescfe.bookpublishing.auth.domain.model.enum.Role
import org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config.AuthProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID

class ConfigUserRepositoryTest {
    @Test
    fun `should resolve user by username`() {
        val authProperties = AuthProperties()
        val userConfig = AuthProperties.User()
        userConfig.username = "user@example.com"
        userConfig.password = "secret"
        userConfig.roles = listOf("ADMIN", "USER")
        authProperties.users = listOf(userConfig)

        val repository = ConfigUserRepository(authProperties)

        val account = repository.findByUsername(Username("user@example.com"))

        requireNotNull(account)
        assertEquals(Username("user@example.com"), account.username)
        assertEquals(setOf(Role.ADMIN, Role.USER), account.roles)
    }

    @Test
    fun `should resolve user by id`() {
        val authProperties = AuthProperties()
        val userConfig = AuthProperties.User()
        userConfig.username = "user@example.com"
        userConfig.password = "secret"
        authProperties.users = listOf(userConfig)

        val repository = ConfigUserRepository(authProperties)
        val expectedId = UserId(UUID.nameUUIDFromBytes("user@example.com".toByteArray()))

        val account = repository.findById(expectedId)

        requireNotNull(account)
        assertEquals(expectedId, account.id)
    }

    @Test
    fun `should ignore users with missing username or password`() {
        val missingUsername =
            AuthProperties.User().apply {
                password = "secret"
                roles = listOf("USER")
            }
        val missingPassword =
            AuthProperties.User().apply {
                username = "user@example.com"
                roles = listOf("USER")
            }

        val authProperties = AuthProperties()
        authProperties.users = listOf(missingUsername, missingPassword)

        val repository = ConfigUserRepository(authProperties)

        assertNull(repository.findByUsername(Username("user@example.com")))
    }

    @Test
    fun `should ignore unknown roles`() {
        val authProperties = AuthProperties()
        val userConfig = AuthProperties.User()
        userConfig.username = "user@example.com"
        userConfig.password = "secret"
        userConfig.roles = listOf("ADMIN", "UNKNOWN")
        authProperties.users = listOf(userConfig)

        val repository = ConfigUserRepository(authProperties)

        val account = repository.findByUsername(Username("user@example.com"))

        requireNotNull(account)
        assertEquals(setOf(Role.ADMIN), account.roles)
    }
}
