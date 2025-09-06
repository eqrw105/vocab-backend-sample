package domain.service

import domain.model.session.AccessTokenIssued
import domain.model.session.DecodedToken
import domain.model.session.ParsedRefresh
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
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim

class TokenService(
    secret: ByteArray,
    private val issuer: String,
    private val clock: Clock = Clock.systemUTC(),
) {
    private val algorithm = Algorithm.HMAC256(secret)
    private val verifier =
        JWT
            .require(algorithm)
            .withIssuer(issuer)
            .acceptLeeway(2) // 초 단위 시계 오차 허용
            .build()

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

    fun verifyAccessToken(token: String): DecodedToken? =
        try {
            val decoded = verifier.verify(token)
            val subject = decoded.subject ?: return null
            val exp = decoded.expiresAt?.time ?: return null
            val claims =
                decoded.claims
                    .filterKeys { it != "iss" && it != "sub" && it != "exp" && it != "iat" && it != "jti" }
                    .mapValues { (_, c) -> c.toPrimitive() }

            DecodedToken(
                subject = subject,
                claims = claims,
                expiresAt = exp,
            )
        } catch (e: JWTVerificationException) {
            null
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

    fun parseRefreshToken(raw: String): ParsedRefresh? {
        val parts = raw.split('.', limit = 2)
        if (parts.size != 2) return null
        val id = runCatching { UUID.fromString(parts[0]) }.getOrNull() ?: return null
        return ParsedRefresh(id, parts[1])
    }

    fun hashRefreshSecret(secret: String): String = sha256Url(secret)

    fun constantTimeEquals(
        a: String,
        b: String,
    ): Boolean {
        val ba = a.toByteArray(StandardCharsets.UTF_8)
        val bb = b.toByteArray(StandardCharsets.UTF_8)
        var r = 0
        val n = maxOf(ba.size, bb.size)
        for (i in 0 until n) {
            val x = if (i < ba.size) ba[i] else 0
            val y = if (i < bb.size) bb[i] else 0
            r = r or (x.toInt() xor y.toInt())
        }
        return r == 0
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

    private fun Claim.toPrimitive(): Any? {
        // 순서대로 시도하여 가장 자연스러운 원시 타입으로 반환
        return try {
            this.asBoolean()
        } catch (_: Exception) {
            null
        }
            ?: try {
                this.asLong()
            } catch (_: Exception) {
                null
            }
            ?: try {
                this.asInt()
            } catch (_: Exception) {
                null
            }
            ?: try {
                this.asDouble()
            } catch (_: Exception) {
                null
            }
            ?: try {
                this.asString()
            } catch (_: Exception) {
                null
            }
    }

    companion object {
        val ACCESS_TOKEN_TTL = 1.hours
        val REFRESH_TOKEN_TTL = 30.days
    }
}
