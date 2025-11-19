package org.cescfe.bookpublishing.shared.infrastructure.adapters.output.persistence.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

@TestConfiguration
@EnableJpaAuditing
class TestJpaAuditingConfig {
    @Bean
    fun testAuditorProvider(): AuditorAware<String> = AuditorAware { Optional.of("test-user") }
}
