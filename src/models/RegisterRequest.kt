package com.example.models

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)