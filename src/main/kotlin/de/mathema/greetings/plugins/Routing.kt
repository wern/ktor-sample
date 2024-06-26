package de.mathema.greetings.plugins

import de.mathema.greetings.auth.authenticationRouting
import de.mathema.greetings.greetingRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*


fun Application.configureRouting() {
    routing {
        authenticationRouting()
        greetingRouting()
    }
}
