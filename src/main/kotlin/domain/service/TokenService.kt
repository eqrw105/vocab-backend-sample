package domain.service

import domain.model.session.AccessTokenIssued
import domain.model.session.RefreshTokenIssued
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Clock
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class TokenService(
    secret: ByteArray,
    private val issuer: String,
    private val clock: Clock = Clock.systemUTC(),
) {
    private val algorithm = Algorithm.HMAC256(secret)

    fun issueAccessToken(
        subject: String,
        claims: Map<String, Any>,
        ttlSeconds: Long,
    ): AccessTokenIssued {
        val now = Instant.now(clock)
        val exp = now.plusSeconds(ttlSeconds)

        val builder =
            JWT
                .create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .withJWTId(UUID.randomUUID().toString())

        // 클레임 설정(문자열/숫자/불리언 중심)
        claims.forEach { (k, v) ->
            when (v) {
                is String -> builder.withClaim(k, v)
                is Int -> builder.withClaim(k, v)
                is Long -> builder.withClaim(k, v)
                is Boolean -> builder.withClaim(k, v)
                is Double -> builder.withClaim(k, v)
                is Float -> builder.withClaim(k, v.toDouble())
                else -> builder.withClaim(k, v.toString())
            }
        }

        val jwt = builder.sign(algorithm)
        return AccessTokenIssued(
            value = jwt,
            expiresAt = exp.toEpochMilli(),
        )
    }

    fun issueRefreshToken(ttlSeconds: Long): RefreshTokenIssued {
        val now = Instant.now(clock)
        val exp = now.plusSeconds(ttlSeconds)
        val tokenId = UUID.randomUUID()
        val secret = randomSecret(32)
        val raw = "$tokenId.$secret"
        val hash = sha256Url(secret)
        return RefreshTokenIssued(
            raw = raw,
            expiresAt = exp.toEpochMilli(),
            id = tokenId,
            secretHash = hash,
        )
    }

    private fun randomSecret(bytes: Int): String {
        val buf = ByteArray(bytes)
        Random.nextBytes(buf)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf)
    }

    private fun sha256Url(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }

    companion object {
        val ACCESS_TOKEN_TTL = 1.hours
        val REFRESH_TOKEN_TTL = 30.days
    }
}
