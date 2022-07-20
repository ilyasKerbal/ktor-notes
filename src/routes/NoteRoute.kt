package io.github.ilyaskerbal.noteappktor.routes

import io.github.ilyaskerbal.noteappktor.data.getNotesForUser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.noteRoute() {
    route("/getNotes") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = getNotesForUser(email)
                call.respond(HttpStatusCode.OK, notes)
            }
        }
    }
}