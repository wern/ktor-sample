# ktor-sample
Code samples from my Talk "Developing REST APIs with Ktor"

## Sample Applications
* `de.mathema.hello.HelloApplication.kt`: A simple Ktor Hello World Endpoint - everything in one function
* `de.mathema.greetings.Application.kt`: A HTTP Service to manage greetings - using the `embeddedServer`
* `de.mathema.greetings.appconf.ApplicationUsingConfigFile.kt`: A HTTP Service to manage greetings - using configuration via `application.conf` (config file needs to be enabled first :warning: )

## Testing Ktor applications
* `de.mathema.greetings.ApplicationTest.kt`: Test using 'in code configuration' for test setup - does not suceed if application.conf is enabled :warning:
* `de.mathema.greetings.appconf.ApplicationUsingConfigFileTest.kt`: Test using config files for test setup
