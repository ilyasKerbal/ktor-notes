package io.github.ilyaskerbal.noteappktor.data

import io.github.ilyaskerbal.noteappktor.data.collections.Note
import io.github.ilyaskerbal.noteappktor.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.insertOne
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