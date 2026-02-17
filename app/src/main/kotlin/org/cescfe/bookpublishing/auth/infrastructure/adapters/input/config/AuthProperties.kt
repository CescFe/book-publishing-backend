package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "auth")
class AuthProperties {
    var users: List<User> = ArrayList()

    class User {
        var username: String? = null
        var password: String? = null
        var roles: List<String> = ArrayList()
    }
}
