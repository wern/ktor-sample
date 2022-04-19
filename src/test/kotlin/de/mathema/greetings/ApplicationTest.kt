package de.mathema.greetings

import de.mathema.greetings.models.Greeting
import de.mathema.greetings.models.greetingStore
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import de.mathema.greetings.plugins.*

// Succeeds only with disabled application.conf.disabled!
class ApplicationTest {
    @Test
    fun testStartWithEmptyGreetingStorage() = testApplication {
        application {
            configureRouting()
        }
        client.get("/greetings").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertEquals("No greetings found", bodyAsText())
        }
    }

    @Test
    fun testReturnAllGreetingsStored() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            storeGreeting(greetingStore, Greeting("casual","Hey there!"))
            storeGreeting(greetingStore,  Greeting("formal","Good morning!"))
        }

        client.get("/greetings").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""[{"type":"casual","greeting":"Hey there!"},{"type":"formal","greeting":"Good morning!"}]""", bodyAsText())
        }
    }

    private fun storeGreeting(store:MutableMap<String,Greeting>, greeting :Greeting) = store.put(greeting.type, greeting)

    @Test
    fun testCreateAGreeting() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/greetings") {
            contentType(ContentType.Application.Json)
            setBody("""{"type": "casual","greeting": "Hey there!"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}