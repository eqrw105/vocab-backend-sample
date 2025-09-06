package domain.service

import application.model.ApiException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.MissingClaimException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import java.security.interfaces.RSAPublicKey

class AppCheckService(
    firebaseProjectNumber: String,
) {
    private val issuer = "https://firebaseappcheck.googleapis.com/$firebaseProjectNumber"
    private val audience = "projects/$firebaseProjectNumber"

    fun verifyAndExtract(
        token: String,
        publicKeyMap: Map<String, RSAPublicKey>,
    ): String {
        val decoded = JWT.decode(token)
        val kid = decoded.keyId ?: throw ApiException.ValidationException("Missing JWS 'kid' header")

        val publicKey = publicKeyMap[kid] ?: throw ApiException.ForbiddenException("No JWK found for kid=$kid")

        val algorithm = Algorithm.RSA256(publicKey, null)
        val verifier =
            JWT
                .require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .acceptLeeway(2) // exp/nbf clock skew 허용(2초)
                .build()

        val jwt =
            try {
                verifier.verify(token)
            } catch (_: AlgorithmMismatchException) {
                throw ApiException.ForbiddenException()
            } catch (_: SignatureVerificationException) {
                throw ApiException.ForbiddenException()
            } catch (_: MissingClaimException) {
                throw ApiException.ForbiddenException()
            } catch (_: IncorrectClaimException) {
                throw ApiException.ForbiddenException()
            } catch (_: TokenExpiredException) {
                throw ApiException.UnauthorizedException()
            }
        return jwt.subject.takeIf { it.isNotBlank() } ?: throw ApiException.ForbiddenException()
    }
}
