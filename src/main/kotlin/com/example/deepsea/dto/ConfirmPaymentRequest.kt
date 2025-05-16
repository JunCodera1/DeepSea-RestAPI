package com.example.deepsea.dto

data class ConfirmPaymentRequest(
    val userId: Long,
    val paymentIntentId: String
)