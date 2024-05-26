@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.routes

import com.example.models.Note
import com.example.models.SimpleResponse
import com.example.models.User
import com.example.repository.Repo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val NOTES = "/notes"
const val NOTE = "/notes/{id}"

@Location(NOTES)
class NotesRoute

@Location(NOTE)
class NoteRoute(val id: String)


@KtorExperimentalLocationsAPI
fun Route.noteRoutes(db: Repo) {
    authenticate("jwt") {

        get<NotesRoute> {
            try {
                val id = call.principal<User>()!!.id
                val notes = db.getAllNotes(id)
                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<Note>())
            }
        }

        post<NotesRoute> {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try {
                val userID = call.principal<User>()!!.id
                db.addNote(note, userID)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note added"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Unexpected Behavior"))
            }
        }

        put<NotesRoute> {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@put
            }
            
            try {
                val userID = call.principal<User>()!!.id
                db.updateNote(note, userID)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully!"))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
            }

        }

        delete<NoteRoute> { noteRoute ->
            val noteId = noteRoute.id
            try {
                val userID = call.principal<User>()!!.id
                db.deleteNote(noteId, userID)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem Occurred!"))
            }
        }
    }
}