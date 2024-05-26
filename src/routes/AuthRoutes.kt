@file:OptIn(KtorExperimentalLocationsAPI::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.routes

import com.example.models.LoginRequest
import com.example.models.RegisterRequest
import com.example.models.SimpleResponse
import com.example.models.User
import com.example.repository.Repo
import com.example.services.JWTService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val REGISTER_REQUEST = "/register"
const val LOGIN_REQUEST = "/login"

@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute

@KtorExperimentalLocationsAPI
fun Route.authRoutes(db: Repo, jwtService: JWTService, hashFunction: (String) -> String) {

    post<UserRegisterRoute> {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing data"))
            return@post
        }

        try {
            val existingUser = db.findUserByEmail(registerRequest.email)
            if (existingUser != null) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "Email already exists"))
                return@post
            }
            val user = User(registerRequest.email, registerRequest.username, hashFunction(registerRequest.password))
            db.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
        }
    }

    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Data"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)
            if (user == null)
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email Id"))
            else if (user.password == hashFunction(loginRequest.password))
                call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
            else
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Password Incorrect!"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred"))
        }
    }
}