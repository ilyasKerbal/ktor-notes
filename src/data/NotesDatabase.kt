package io.github.ilyaskerbal.noteappktor.data

import io.github.ilyaskerbal.noteappktor.data.collections.Note
import io.github.ilyaskerbal.noteappktor.data.collections.User
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private const val DATABASE_NAME = "NotesDatabase"

private val client = KMongo.createClient().coroutine // we can specify the connection string
private val database = client.getDatabase(DATABASE_NAME)
private val userCollection = database.getCollection<User>()
private val noteCollection = database.getCollection<Note>()

suspend fun insertUser(user: User) : Boolean = userCollection.insertOne(user).wasAcknowledged()

suspend fun checkIFUserExists(email: String) : Boolean {
    // we can also use a string as a filter: {email: $email}
    return userCollection.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, password: String) : Boolean {
    val actualPassword = userCollection.findOne(User::email eq email)?.password ?: return false
    return actualPassword == password
}

suspend fun getNotesForUser(email: String) : List<Note> {
    return noteCollection.find(Note::owners contains email).toList()
}

suspend fun saveNote(note: Note) : Boolean {
    val noteExists = note.id != null &&  noteCollection.findOneById(note.id) != null
    return if (noteExists) {
        noteCollection.updateOneById(note.id, note).wasAcknowledged()
    } else {
        noteCollection.insertOne(note).wasAcknowledged()
    }
}