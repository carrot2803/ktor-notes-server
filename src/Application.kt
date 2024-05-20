package com.example

import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import com.example.routes.authRoutes
import com.example.routes.noteRoutes
import com.example.services.JWTService
import com.example.services.hash
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JWTService()
    val hashFunction = { s: String -> hash(s) }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "Note Server"
            validate {
                val payload = it.payload
                val id = payload.getClaim("id").asInt()
                val user = db.findUserByID(id)
                user
            }
        }
    }
    install(ContentNegotiation) { gson {} }
    install(Locations)

    routing {
        get("/") {
            call.respondText("FELICIA IS AN EGG", contentType = ContentType.Text.Plain)
        }
        authRoutes(db, jwtService, hashFunction)
        noteRoutes(db)
    }
}

data class MySession(val count: Int = 0)

