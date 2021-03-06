package de.mathema.greetings

import kotlinx.serialization.Serializable

@Serializable
data class Greeting(val type:String, val greeting: String)

val greetingStore = mutableMapOf<String, Greeting>()