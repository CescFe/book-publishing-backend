package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.config

import org.cescfe.bookpublishing.auth.infrastructure.adapters.output.security.UserService
import org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security.JwtRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter,
) {
    @Bean
    fun authenticationProvider(
        userService: UserService,
        passwordEncoder: PasswordEncoder,
    ): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider(userService)
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(HttpMethod.POST, "/api/v1/authors")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/authors/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/authors/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/authors")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/authors/*")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v1/books")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/books/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/books/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/books")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/books/*")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v1/collections")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/collections/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/collections/*")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/collections")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/collections/*")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/v1/auth/login")
                    .permitAll()
                    .requestMatchers("/api/v1/health")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**")
                    .permitAll()
                    .requestMatchers("/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
