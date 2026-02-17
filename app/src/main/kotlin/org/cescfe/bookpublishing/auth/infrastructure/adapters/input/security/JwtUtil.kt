package org.cescfe.bookpublishing.auth.infrastructure.adapters.input.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Value("\${jwt.expiration:900}")
    private var expiration: Long = 900

    private val key: SecretKey by lazy {
        require(secret.length >= 32) {
            "JWT secret must be at least 32 characters long for security"
        }
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration * 1000)

        return Jwts
            .builder()
            .subject(userDetails.username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String = getClaimsFromToken(token).subject

    fun getExpirationTime(): Long = expiration

    fun getExpirationInstant(token: String): Instant =
        getClaimsFromToken(token)
            .expiration
            .toInstant()

    fun isTokenExpired(token: String): Boolean {
        val expiration = getClaimsFromToken(token).expiration
        return expiration.before(Date())
    }

    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun getClaimsFromToken(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}
