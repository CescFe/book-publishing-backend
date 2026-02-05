package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.cescfe.bookpublishing.auth.application.port.output.TokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant

@Component
class JwtRequestFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if (SecurityContextHolder.getContext().authentication != null) {
                filterChain.doFilter(request, response)
                return
            }

            val token = authHeader.substring(7)

            try {
                val payload = tokenService.parseToken(token)
                if (payload.expiresAt.isAfter(Instant.now())) {
                    val userDetails = userDetailsService.loadUserByUsername(payload.username.value)
                    val authToken =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities,
                        )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            } catch (_: Exception) {
            }
        }

        filterChain.doFilter(request, response)
    }
}
