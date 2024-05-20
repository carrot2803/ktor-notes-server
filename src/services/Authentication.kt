package com.example.services

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private val macKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(password: String): String {
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(macKey)
    return hex(mac.doFinal(password.toByteArray(Charsets.UTF_8)))
}