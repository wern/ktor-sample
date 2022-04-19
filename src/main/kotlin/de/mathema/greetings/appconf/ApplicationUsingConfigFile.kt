package de.mathema.greetings.appconf

import de.mathema.greetings.plugins.configureRouting
import de.mathema.greetings.plugins.configureSerialization
import io.ktor.server.application.*

// bootstrapping using application.conf.disabled - rename file in resources folder to enable config

fun main (args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    // these configs are global!!!
    configureRouting()
    configureSerialization()
}
