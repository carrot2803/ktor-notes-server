package com.example.repository

import com.example.models.Note
import com.example.models.User
import com.example.repository.DatabaseFactory.dbQuery
import com.example.repository.tables.NoteTable
import com.example.repository.tables.UserTable
import org.jetbrains.exposed.sql.*


class Repo {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[email] = user.email
                ut[username] = user.username
                ut[password] = user.password
            }
        }
    }

    suspend fun findUserByID(id: Int) = dbQuery {
        UserTable.select { UserTable.id.eq(id) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null)
            return null
        return User(row[UserTable.email], row[UserTable.username], row[UserTable.password], row[UserTable.id])
    }

    suspend fun getAllNotes(id: Int): List<Note> =
        dbQuery { NoteTable.select { NoteTable.userID.eq(id) }.mapNotNull { rowToNote(it) } }

    suspend fun addNote(note: Note, userID: Int) {
        dbQuery {
            NoteTable.insert { nt ->
                nt[NoteTable.userID] = userID
                nt[title] = note.title
                nt[description] = note.description
                nt[date] = note.date
            }
        }
    }

    suspend fun updateNote(note: Note, userID: Int) {
        dbQuery {
            NoteTable.update(where = { NoteTable.userID.eq(userID) and NoteTable.id.eq(note.id) }) { nt ->
                nt[title] = note.title
                nt[description] = note.description
                nt[date] = note.date
            }
        }
    }

    suspend fun deleteNote(id: Int, userID: Int) {
        dbQuery {
            NoteTable.deleteWhere { NoteTable.userID.eq(userID) and NoteTable.id.eq(id) }
        }
    }

    private fun rowToNote(row: ResultRow?): Note? {
        if (row == null)
            return null
        println(NoteTable.userID)
        return Note(row[NoteTable.id], row[NoteTable.title], row[NoteTable.description], row[NoteTable.date])
    }
}