package com.example.deepsea.dto

data class ConfirmPaymentResponse(
    val success: Boolean,
    val subscriptionId: String?
)