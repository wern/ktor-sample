package de.mathema.greetings

import de.mathema.greetings.auth.config.authSecret
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import de.mathema.greetings.plugins.*

// Succeeds only with disabled application.conf.disabled!
class GreetingApplicationTest {
    @Test
    fun testGetWithEmptyGreetingStorage() = testApplication {
        application {
            configureBearerTokenAuthentication()
            configureRouting()
            configureSerialization()
            greetingStore.clear()
        }
        client.get("/greetings"){
            headers {
                header("Authorization", "Bearer $authSecret")
            }
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
    }

    @Test
    fun testGetReturnsAllGreetingsStored() = testApplication {
        application {
            configureBearerTokenAuthentication()
            configureRouting()
            configureSerialization()
            greetingStore.clear()
            storeGreeting(greetingStore, Greeting("casual","Hey there!"))
            storeGreeting(greetingStore,  Greeting("formal","Good morning!"))
        }

        client.get("/greetings"){
            headers {
                header("Authorization", "Bearer $authSecret")
            }
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""[{"type":"casual","greeting":"Hey there!"},{"type":"formal","greeting":"Good morning!"}]""", bodyAsText())
        }
    }

    private fun storeGreeting(store:MutableMap<String,Greeting>, greeting :Greeting) = store.put(greeting.type, greeting)

    @Test
    fun testCreateAGreeting() = testApplication {
        application {
            configureBearerTokenAuthentication()
            configureRouting()
            configureSerialization()
        }

        val response = client.post("/greetings") {
            contentType(ContentType.Application.Json)
            setBody("""{"type": "casual","greeting": "Hey there!"}""")
            headers {
                header("Authorization", "Bearer $authSecret")
            }
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}