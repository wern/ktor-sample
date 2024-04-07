package de.mathema.greetings.auth.config

// Move to environment config in real life project!!!
val authRealm = "Ktor Workshop Sample Realm"
val authUser = "Ktor"
val authSecret = "KtorWorkshop"
val authIssuer = "http://0.0.0.0:8080/"
val authAudience = "http://0.0.0.0:8080/greetings"