package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config

import org.cescfe.bookpublishing.auth.application.port.output.Clock
import org.cescfe.bookpublishing.auth.application.port.output.PasswordHasher
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.cescfe.bookpublishing.auth.application.port.output.UserRepository
import org.cescfe.bookpublishing.auth.domain.policy.ScopePolicy
import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.persistence.ConfigUserRepository
import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security.JwtTokenService
import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security.SpringPasswordHasher
import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.time.SystemClock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthAdapterConfig {
    @Bean
    fun userRepository(impl: ConfigUserRepository): UserRepository = impl

    @Bean
    fun passwordHasher(impl: SpringPasswordHasher): PasswordHasher = impl

    @Bean
    fun tokenService(impl: JwtTokenService): TokenService = impl

    @Bean
    fun clock(): Clock = SystemClock()

    @Bean
    fun scopePolicy(): ScopePolicy = ScopePolicy()
}
