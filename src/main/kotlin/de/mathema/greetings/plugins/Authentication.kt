package de.mathema.greetings.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.mathema.greetings.auth.config.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureBasicAuthentication() {
    install(Authentication) {
        basic("auth-basic") {
            realm = authRealm
            validate { credentials ->
                if (credentials.name == authUser && credentials.password == authSecret) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureBearerTokenAuthentication(){
    install(Authentication) {
        bearer("auth-bearer") {
            realm = authRealm
            authenticate { tokenCredential ->
                if (tokenCredential.token == authSecret) {
                    UserIdPrincipal(authUser)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureJWTAuthentication(){

    install(Authentication) {
        jwt ("auth-jwt"){
            realm = authRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authSecret))
                    .withAudience(authAudience)
                    .withIssuer(authIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() == authUser) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}