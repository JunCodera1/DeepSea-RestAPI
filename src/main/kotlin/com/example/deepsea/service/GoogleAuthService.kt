package com.example.deepsea.service

import com.example.deepsea.model.GoogleUserInfo
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class GoogleAuthService(
    @Value("\${google.client.id}")
    private val clientId: String
) {
    private val transport = NetHttpTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Xác thực token Google và trả về thông tin người dùng
     */
    fun verifyGoogleToken(idTokenString: String): GoogleUserInfo? {
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(clientId))
            .build()

        try {
            val idToken: GoogleIdToken = verifier.verify(idTokenString) ?: return null
            val payload = idToken.payload

            return GoogleUserInfo(
                id = payload.subject,
                email = payload["email"] as String,
                verified_email = payload["email_verified"] as Boolean,
                name = payload["name"] as String,
                given_name = payload["given_name"] as? String,
                family_name = payload["family_name"] as? String,
                picture = payload["picture"] as? String
            )
        } catch (e: Exception) {
            // Log lỗi nếu có
            e.printStackTrace()
            return null
        }
    }
}