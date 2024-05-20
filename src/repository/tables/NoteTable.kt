package com.example.repository.tables

import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {
    val id = integer("id").autoIncrement()
    val userID = integer("userID").references(UserTable.id)
    val title = text("title")
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}