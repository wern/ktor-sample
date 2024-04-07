package de.mathema.greetings.plugins

import de.mathema.greetings.plugins.custom.*
import io.ktor.server.application.*

fun Application.configureCustomPlugin() {
    install(TypeAlwaysLowerCase){}
}