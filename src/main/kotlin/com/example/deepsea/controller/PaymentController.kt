package com.example.deepsea.controller

import com.example.deepsea.dto.ConfirmPaymentRequest
import com.example.deepsea.dto.ConfirmPaymentResponse
import com.example.deepsea.dto.PaymentIntentRequest
import com.example.deepsea.dto.PaymentIntentResponse
import com.example.deepsea.service.StripeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val stripeService: StripeService) {

    @PostMapping("/intent")
    suspend fun createPaymentIntent(@RequestBody request: PaymentIntentRequest): PaymentIntentResponse {
        val paymentIntent = stripeService.createPaymentIntent(request.userId, request.amount, request.currency)
        return PaymentIntentResponse(clientSecret = paymentIntent.clientSecret)
    }

    @PostMapping("/confirm")
    suspend fun confirmPayment(@RequestBody request: ConfirmPaymentRequest): ConfirmPaymentResponse {
        val success = stripeService.confirmPayment(request.userId, request.paymentIntentId)
        val subscriptionId = if (success) "sub_${request.userId}" else null
        return ConfirmPaymentResponse(success = success, subscriptionId = subscriptionId)
    }
}