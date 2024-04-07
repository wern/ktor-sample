package de.mathema.greetings

import de.mathema.greetings.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureCustomPlugin()
        configureBearerTokenAuthentication()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}