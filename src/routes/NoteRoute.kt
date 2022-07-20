package io.github.ilyaskerbal.noteappktor.routes

import io.github.ilyaskerbal.noteappktor.data.collections.Note
import io.github.ilyaskerbal.noteappktor.data.deleteNoteForUser
import io.github.ilyaskerbal.noteappktor.data.getNotesForUser
import io.github.ilyaskerbal.noteappktor.data.requests.DeleteNoteRequest
import io.github.ilyaskerbal.noteappktor.data.saveNote
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
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

    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val saveStatus = saveNote(note)
                call.respond(HttpStatusCode.OK, saveStatus)
            }
        }
    }

    route("/deleteNote") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val deleteRequest = try {
                    call.receive<DeleteNoteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val deleteStatus = deleteNoteForUser(email, deleteRequest.id)
                if (deleteStatus)
                    call.respond(HttpStatusCode.OK, "Note deleted successfully")
                else call.respond(HttpStatusCode.OK, "Unable to delete Note, try again")
            }
        }
    }
}