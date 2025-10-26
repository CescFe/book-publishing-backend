package org.cescfe.bookpublishing.shared.infrastructure.adapters.output.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@Configuration
@EnableJpaAuditing
class JpaAuditingConfig {
    @Bean
    fun auditorProvider(): AuditorAware<String> =
        AuditorAware {
            try {
                val authentication = SecurityContextHolder.getContext().authentication
                if (authentication != null && authentication.isAuthenticated) {
                    Optional.of(authentication.name)
                } else {
                    Optional.of("system")
                }
            } catch (_: Exception) {
                Optional.of("system")
            }
        }
}
