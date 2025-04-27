package com.example.deepsea.service

import com.example.deepsea.model.PasswordResetToken
import com.example.deepsea.repository.PasswordResetTokenRepository
import com.example.deepsea.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val tokenRepository: PasswordResetTokenRepository,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder
) {

    fun requestPasswordReset(email: String): Boolean {
        val userOptional = userRepository.findByEmail(email)

        if (userOptional.isPresent) {
            // Generate a 5-digit verification code
            val verificationCode = generateVerificationCode()

            // Save the token to database
            val resetToken = PasswordResetToken(
                email = email,
                verificationCode = verificationCode
            )
            tokenRepository.save(resetToken)

            // Send email with verification code
            emailService.sendPasswordResetEmail(email, verificationCode)

            return true
        }

        return false
    }

    fun verifyCode(email: String, code: String): Boolean {
        val tokenOptional = tokenRepository.findByEmailAndVerificationCode(email, code)

        if (tokenOptional.isPresent) {
            val token = tokenOptional.get()

            // Check if token is expired
            if (token.expiryDate.isAfter(LocalDateTime.now()) && !token.used) {
                return true
            }
        }

        return false
    }

    fun resetPassword(email: String, code: String, newPassword: String): Boolean {
        val tokenOptional = tokenRepository.findByEmailAndVerificationCode(email, code)

        if (tokenOptional.isPresent) {
            val token = tokenOptional.get()

            // Check if token is expired
            if (token.expiryDate.isAfter(LocalDateTime.now()) && !token.used) {
                val userOptional = userRepository.findByEmail(email)

                if (userOptional.isPresent) {
                    val user = userOptional.get()
                    // Update user password
                    user.updatePassword(passwordEncoder.encode(newPassword))
                    userRepository.save(user)
                    // Mark token as used
                    token.used = true
                    tokenRepository.save(token)

                    return true
                }
            }
        }

        return false
    }

    private fun generateVerificationCode(): String {
        return Random.nextInt(10000, 100000).toString()
    }
}