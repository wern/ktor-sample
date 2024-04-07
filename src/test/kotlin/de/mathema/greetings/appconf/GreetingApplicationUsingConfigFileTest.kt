package de.mathema.greetings.appconf

import de.mathema.greetings.Greeting
import de.mathema.greetings.auth.config.authSecret
import de.mathema.greetings.greetingStore
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlin.test.*
import io.ktor.server.testing.*

fun Application.testData() {
    greetingStore["casual"] = Greeting("casual","Hey there!")
    greetingStore["formal"] = Greeting("formal","Good morning!")
}

fun Application.noData() {
    greetingStore.clear()
}

class ApplicationUsingConfigFileTest {
    @Test
    fun testGetWithEmptyGreetingStorage() = testApplication {
        environment {
            config = ApplicationConfig("application-test-nodata.conf")
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
        environment {
            config = ApplicationConfig("application-test.conf")
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
        environment {
            config = ApplicationConfig("application-test-nodata.conf")
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