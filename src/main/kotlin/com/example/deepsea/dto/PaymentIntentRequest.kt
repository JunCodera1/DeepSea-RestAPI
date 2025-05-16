package com.example.deepsea.dto

data class PaymentIntentRequest(
    val userId: Long,
    val amount: Int,
    val currency: String
)