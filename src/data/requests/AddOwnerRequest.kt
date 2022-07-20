package io.github.ilyaskerbal.noteappktor.data.requests

data class AddOwnerRequest(
    val noteId: String,
    val owner: String
)
