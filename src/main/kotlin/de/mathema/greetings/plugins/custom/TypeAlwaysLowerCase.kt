package de.mathema.greetings.plugins.custom

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.*

private val json = Json { ignoreUnknownKeys = true }

val TypeAlwaysLowerCase = createApplicationPlugin(name = "TypeAlwaysLowerCase") {
    println("TypeAlwaysLowerCasePlugin is installed!")

    onCall {
        println("onCall() received")
    }

    onCallReceive { call ->
        transformBody { data ->
            if(call.request.origin.uri == "/greetings") {
                val body = data.readUTF8Line()?:""
                try {
                    val type = json.parseToJsonElement(body).jsonObject.get("type")?.jsonPrimitive?.content ?: ""
                    ByteReadChannel(body.replace(type, type.lowercase()))
                }catch(ex : Exception){
                    ByteReadChannel(body)
                }
            } else {
                data
            }
        }
    }
}