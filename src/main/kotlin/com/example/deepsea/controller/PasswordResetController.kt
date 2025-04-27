package com.example.deepsea.controller

import com.example.deepsea.service.PasswordResetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/password-reset")
class PasswordResetController(private val passwordResetService: PasswordResetService) {

    data class EmailRequest(val email: String)
    data class VerifyRequest(val email: String, val code: String)
    data class ResetRequest(val email: String, val code: String, val newPassword: String)

    @PostMapping("/request")
    fun requestReset(@RequestBody request: EmailRequest): ResponseEntity<Map<String, Any>> {
        val success = passwordResetService.requestPasswordReset(request.email)

        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "Reset email sent"))
        } else {
            ResponseEntity.ok(mapOf("success" to false, "message" to "Email not found"))
        }
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestBody request: VerifyRequest): ResponseEntity<Map<String, Any>> {
        val isValid = passwordResetService.verifyCode(request.email, request.code)

        return if (isValid) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "Code verified"))
        } else {
            ResponseEntity.ok(mapOf("success" to false, "message" to "Invalid or expired code"))
        }
    }

    @PostMapping("/reset")
    fun resetPassword(@RequestBody request: ResetRequest): ResponseEntity<Map<String, Any>> {
        val success = passwordResetService.resetPassword(request.email, request.code, request.newPassword)

        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "Password reset successful"))
        } else {
            ResponseEntity.ok(mapOf("success" to false, "message" to "Password reset failed"))
        }
    }
}