package io.github.ilyaskerbal.noteappktor.routes

import io.github.ilyaskerbal.noteappktor.data.checkIFUserExists
import io.github.ilyaskerbal.noteappktor.data.collections.User
import io.github.ilyaskerbal.noteappktor.data.insertUser
import io.github.ilyaskerbal.noteappktor.data.requests.AccountRequest
import io.github.ilyaskerbal.noteappktor.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (!checkIFUserExists(request.email)){
                if (
                    insertUser(User(
                        request.email,
                        request.password
                    ))
                ) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Successful registration, You can log in"))
                } else {
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "An unknown error occurred, try again"))
                }
            } else {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "You have already registered with this e-mail"))
            }
        }
    }
}