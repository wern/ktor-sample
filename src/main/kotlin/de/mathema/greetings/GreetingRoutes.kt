package de.mathema.greetings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.greetingRouting(){
    route("/greetings") {
        authenticate("auth-bearer") {
            get {
                call.respond(greetingStore.map { e -> e.value })
            }

            get("{type}") {
                val type = call.parameters["type"]
                call.respond(greetingStore[type] ?: HttpStatusCode.NotFound)
            }

            post {
                try {
                    val greeting = call.receive<Greeting>()
                    greetingStore[greeting.type] = greeting
                    call.respond(HttpStatusCode.Created)
                }catch (ex : Exception){
                    call.respond(HttpStatusCode.BadRequest,"Not a valid greeting!")
                }
            }

            delete("{type}") {
                val type = call.parameters["type"]
                greetingStore.remove(type) ?: return@delete call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}