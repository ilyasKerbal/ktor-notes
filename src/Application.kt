package io.github.ilyaskerbal.noteappktor

import io.github.ilyaskerbal.noteappktor.data.checkPasswordForEmail
import io.github.ilyaskerbal.noteappktor.data.collections.User
import io.github.ilyaskerbal.noteappktor.data.insertUser
import io.github.ilyaskerbal.noteappktor.routes.loginRoute
import io.github.ilyaskerbal.noteappktor.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Routing) {
        registerRoute()
        loginRoute()
    }
    install(Authentication) {
        configureAuth()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Notes Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

