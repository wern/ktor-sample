package de.mathema.greetings.routes

import de.mathema.greetings.models.Greeting
import de.mathema.greetings.models.greetingStore
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.greetingRouting(){
    route("/greetings") {
        get {
            if(greetingStore.isNotEmpty()){
                call.respond(greetingStore.map { (type , greet) -> greet })
            }else{
                call.respondText("No greetings found", status = HttpStatusCode.NotFound)
            }
        }

        get("{type?}") {
            val type = call.parameters["type"] ?: return@get call.respondText("Missing type", status = HttpStatusCode.BadRequest)
            val greeting = greetingStore[type] ?: call.respondText("No matching greeting found", status = HttpStatusCode.NotFound)
            call.respond(greeting)
        }
        post {
            val greeting = call.receive<Greeting>()
            greetingStore[greeting.type] = greeting
            call.respondText("Customer created", status = HttpStatusCode.Created)
        }
        delete("{type?}") {
            val type = call.parameters["type"] ?: return@delete call.respondText(
                "Missing type",
                status = HttpStatusCode.BadRequest
            )
            greetingStore.remove(type) ?: return@delete call.respondText("Greeting removed", status = HttpStatusCode.OK)
            call.respondText("No greeting found", status = HttpStatusCode.NotFound)
        }
    }
}