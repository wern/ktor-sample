package de.mathema.greetings.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.mathema.greetings.auth.config.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class User(val username: String, val password: String)
fun Route.authenticationRouting() {
    route("/auth") {
        post("/login") {
            val user = call.receive<User>()
            call.respond(
                if(user.password == authSecret) {
                    val token = JWT.create()
                        .withAudience(authAudience)
                        .withIssuer(authIssuer)
                        .withClaim("username", user.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 300000))
                        .sign(Algorithm.HMAC256(authSecret))
                    hashMapOf("token" to token)
                } else {
                    Unauthorized
                }
            )
        }
    }
}