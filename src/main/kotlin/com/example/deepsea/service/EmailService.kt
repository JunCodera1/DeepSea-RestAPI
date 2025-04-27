package com.example.deepsea.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendPasswordResetEmail(to: String, verificationCode: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject("Password Reset Request")
        message.setText("Your verification code is: $verificationCode")
        mailSender.send(message)
    }
}