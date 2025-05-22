package com.example.deepsea.service

import com.example.deepsea.entity.User
import com.example.deepsea.enums.Role
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userService: UserService,
) {
    fun createToken(user: User): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(30L, ChronoUnit.DAYS))
            .subject(user.username)
            .claim("userId", user.id)
            .claim("role", user.role.name) // Thêm role vào token
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): User? {
        return try {
            val jwt = jwtDecoder.decode(token)
            val userId = jwt.claims["userId"] as Long
            userService.getUserById(userId)
        } catch (e: Exception) {
            null
        }
    }

    fun getUserFromToken(token: String): User? {
        return try {
            // Loại bỏ "Bearer " prefix nếu có
            val cleanToken = if (token.startsWith("Bearer ")) {
                token.substring(7)
            } else {
                token
            }

            val jwt = jwtDecoder.decode(cleanToken)
            val userId = jwt.claims["userId"] as Long
            userService.getUserById(userId)
        } catch (e: Exception) {
            null
        }
    }

    // Method helper để lấy role từ token mà không cần query database
    fun getRoleFromToken(token: String): Role? {
        return try {
            val cleanToken = if (token.startsWith("Bearer ")) {
                token.substring(7)
            } else {
                token
            }

            val jwt = jwtDecoder.decode(cleanToken)
            val roleString = jwt.claims["role"] as String
            Role.valueOf(roleString)
        } catch (e: Exception) {
            null
        }
    }

    // Method để validate token
    fun validateToken(token: String): Boolean {
        return try {
            val cleanToken = if (token.startsWith("Bearer ")) {
                token.substring(7)
            } else {
                token
            }

            val jwt = jwtDecoder.decode(cleanToken)
            // Kiểm tra token chưa hết hạn
            val expirationTime = jwt.expiresAt
            expirationTime?.isAfter(Instant.now()) ?: false
        } catch (e: Exception) {
            false
        }
    }

    // Method để lấy user ID từ token
    fun getUserIdFromToken(token: String): Long? {
        return try {
            val cleanToken = if (token.startsWith("Bearer ")) {
                token.substring(7)
            } else {
                token
            }

            val jwt = jwtDecoder.decode(cleanToken)
            jwt.claims["userId"] as Long
        } catch (e: Exception) {
            null
        }
    }
}