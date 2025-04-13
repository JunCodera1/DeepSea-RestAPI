package com.example.deepsea.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class HashService : PasswordEncoder {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    fun checkBcrypt(password: String, hashedPassword: String): Boolean {
        return passwordEncoder.matches(password, hashedPassword)
    }

    fun hashBcrypt(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun encode(rawPassword: CharSequence): String {
        return passwordEncoder.encode(rawPassword)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
