package com.example.models

import io.ktor.auth.*

data class User(
    val email: String,
    val username: String,
    val password: String,
    val id: Int = 0
) : Principal
