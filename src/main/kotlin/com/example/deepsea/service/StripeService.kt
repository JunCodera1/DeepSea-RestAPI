package com.example.deepsea.service

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StripeService(@Value("\${stripe.secret.key}") private val stripeApiKey: String) {

    @PostConstruct
    fun init() {
        Stripe.apiKey = stripeApiKey
    }

    suspend fun createPaymentIntent(userId: Long, amount: Int, currency: String): PaymentIntent = withContext(Dispatchers.IO) {
        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount.toLong())
            .setCurrency(currency)
            .putMetadata("userId", userId.toString())
            .build()
        PaymentIntent.create(params)
    }

    suspend fun confirmPayment(userId: Long, paymentIntentId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val paymentIntent = PaymentIntent.retrieve(paymentIntentId)
            paymentIntent.status == "succeeded"
        } catch (e: Exception) {
            false
        }
    }
}