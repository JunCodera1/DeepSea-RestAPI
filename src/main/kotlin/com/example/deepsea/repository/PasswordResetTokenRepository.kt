package com.example.deepsea.repository

import com.example.deepsea.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findByToken(token: String): Optional<PasswordResetToken>
    fun findByEmail(email: String): List<PasswordResetToken>
    fun findByVerificationCode(verificationCode: String): Optional<PasswordResetToken>
    fun findByEmailAndVerificationCode(email: String, verificationCode: String): Optional<PasswordResetToken>
}