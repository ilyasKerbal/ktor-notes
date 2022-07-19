package io.github.ilyaskerbal.noteappktor

import io.github.ilyaskerbal.noteappktor.data.collections.User
import io.github.ilyaskerbal.noteappktor.data.insertUser
import io.ktor.application.*
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
    install(Routing)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    CoroutineScope(Dispatchers.IO).launch {
        insertUser(User(
            email = "test@test.test",
            password = "bhfgluDJASDJHadshjdgJHASVHJ",
        ))
    }
}

